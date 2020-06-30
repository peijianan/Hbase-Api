package com.tensor.api.org.service.hbase.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.Page;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import com.tensor.api.org.service.mq.BatchConsumerService;
import com.tensor.api.org.service.mq.ProducerService;
import com.tensor.api.org.util.HBaseUtils;
import com.tensor.api.org.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.LinkedList;
import java.util.List;

import static com.tensor.api.org.util.hbase.MappingFactory.MAPPING_FACTORY;

/**
 * @author peijianan
 */
@Slf4j
@Service(value = "HBaseNewsService")
public class HBaseNewsServiceImpl implements HBaseNewsService, BatchConsumerService<Message> {

    @Autowired
    private HBaseBasicService hBaseBasicService;

    @Autowired
    private ProducerService producerService;

    @PostConstruct
    public void init() {
        producerService.register("HBase-Store", this, 10);
    }

    @Override
    public void onEvent(List<Message> data) {
        for (Message event : data) {
            Mono<ResultData<Boolean>> result = putNews((News) event.getData());
            result.subscribe(booleanResultData -> {
                if (event.getRetryCnt() >= 3) {
                    log.error("[HBase-API Error] 新闻存储失败，已超过重试次数，" + booleanResultData.getMsg());
                    return;
                }
                if (!booleanResultData.getData()) {
                    event.setRetryCnt(event.getRetryCnt() + 1);
                    producerService.publish(event);
                }
            });
        }
    }

    @Override
    public Mono<ResultData<Page>> getAllNews() {
        ResultData resultData;
        try {
            ResultScanner res = hBaseBasicService.scantable(HBaseUtils.TABLE_NAME);
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<Page>> getAllAuthor() {
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.DependentColumnFilter(HBaseUtils.TABLE_NAME,HBaseUtils.cf1,HBaseUtils.cf1_author,false, CompareFilter.CompareOp.NO_OP,null);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);

            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<Page>> getAllTitle() {     //读取全部标题 返回 行键-标题
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.DependentColumnFilter(HBaseUtils.TABLE_NAME,HBaseUtils.cf1,HBaseUtils.cf1_newTitle,false, CompareFilter.CompareOp.NO_OP,null);
            for (Result ress : res) {


                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);

            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<Page>> getNewsByRowKey(String rowKey) {    //根据行键读取新闻 返回对应新闻
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.RowFilter(HBaseUtils.TABLE_NAME, rowKey);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<Page>>getNewsByTitle(String newTitle) {   //根据标题读取新闻  返回对应新闻
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.SingleColumnValueFilter(HBaseUtils.TABLE_NAME,HBaseUtils.cf1,HBaseUtils.cf1_newTitle,newTitle);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);

    }

    @Override
    public Mono<ResultData<Page>> getNewsByType(String newType) {     //根据分类读新闻
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.SingleColumnValueFilter(HBaseUtils.TABLE_NAME,HBaseUtils.cf1,HBaseUtils.cf1_newType,newType);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<Page>> getNewsByAuthor(String author) {    //根据作者读新闻    返回对应新闻
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            ResultScanner res = hBaseBasicService.SingleColumnValueFilter(HBaseUtils.TABLE_NAME,HBaseUtils.cf1,HBaseUtils.cf1_author,author);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            page.setData(newsList);
            page.setTotal(newsList.size());
            resultData = ResultData.buildSuccessFromData(page);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);

    }

    /**
     * 根据传入的参数将新闻插入数据库 返回是否成功
     *
     * @param news
     * @return
     */
    public Mono<ResultData<Boolean>> putNews(News news) {
        ResultData resultData = new ResultData();
        boolean flag;
        try {

            /**
             * 采用新的接口信息 {@link com.tensor.api.org.enpity.ColumnData}
             * 采用新的方法进行遍历插入数据 {@link com.tensor.api.org.util.hbase.MappingFactory#buildColumnData(Object)}
             */

            String id =Long.toString(news.getId());
            boolean a = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_author, news.getAuthor());
            boolean b = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newType, news.getNewType());
            boolean c = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newTitle, news.getNewTitle());
            boolean d = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf2, HBaseUtils.cf2_text, news.getText());
            boolean e = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_publishDate, news.getPublishDate());
            boolean f = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_url,news.getUrl());
            boolean g = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_source,news.getSource());
            boolean h = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_hashcode,news.getHashCode());

            flag = a && b && c && d && e && f && g && h;
            resultData.setData(flag);
            if (flag) {
                resultData.setCode(HttpStatus.OK.value());
            } else {
                resultData.buildFromResultCode(ResultCode.STORAGE_FAILURE);
            }
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }
        return Mono.just(resultData);
    }

}
