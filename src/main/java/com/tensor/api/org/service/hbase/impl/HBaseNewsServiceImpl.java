package com.tensor.api.org.service.hbase.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import com.tensor.api.org.service.mq.ConsumerService;
import com.tensor.api.org.service.mq.ProducerService;
import com.tensor.api.org.util.HBaseUtils;
import com.tensor.api.org.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
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
public class HBaseNewsServiceImpl implements HBaseNewsService, ConsumerService {

    @Autowired
    private HBaseBasicService hBaseBasicService;

    @Autowired
    private ProducerService producerService;

    @PostConstruct
    public void init() {
        producerService.register("HBase-Store", this);
    }

    @Override
    public void onEvent(Message event) throws Exception {
        Object data = event.getData();
        Mono<ResultData<Boolean>> result = putNews((News) data);
        result.subscribe(booleanResultData -> {
            if (event.getRetryCnt() >= 3) {
                log.error("[HBase-API Error] 新闻存储失败，已超过重试次数");
                return;
            }
            if (!booleanResultData.getData()) {
                event.setRetryCnt(event.getRetryCnt() + 1);
                producerService.publish(event);
            }
        });
    }

    @Override
    public Mono<ResultData<List>> getAllNews() {  //读取全部新闻
        ResultData resultData;
        try {
            List<News> newsList = new LinkedList<>();
            ResultScanner res = hBaseBasicService.scantable(HBaseUtils.TABLE_NAME);
            for (Result ress : res) {

                News news = MAPPING_FACTORY.mapToObj(ress, News.class);
                newsList.add(news);
            }
            resultData = ResultData.buildSuccessFromData(newsList);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<JsonArray>> getAllAuthor() {    //读取全部作者 返回 行键-作者
        ResultData resultData;

        try {
            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.QualifierFilter(HBaseUtils.TABLE_NAME, HBaseUtils.cf1_author);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",Bytes.toString(ress.getRow()));
                jsonObject.addProperty("author", Bytes.toString(ress.getValue(Bytes.toBytes(HBaseUtils.cf1), Bytes.toBytes(HBaseUtils.cf1_author))));
                array.add(jsonObject);

            }
            resultData = ResultData.buildSuccessFromData(array);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }
        
        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<JsonArray>> getAllTitle() {     //读取全部标题 返回 行键-标题
        ResultData resultData;
        try {

            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.QualifierFilter(HBaseUtils.TABLE_NAME, HBaseUtils.cf1_newTitle);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", Bytes.toString(ress.getRow()));
                jsonObject.addProperty("newTitle", Bytes.toString(ress.getValue(Bytes.toBytes(HBaseUtils.cf1), Bytes.toBytes(HBaseUtils.cf1_newTitle))));
                array.add(jsonObject);
            }
            resultData = ResultData.buildSuccessFromData(array);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }
        
        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<JsonArray>> getNewsByRowKey(String rowKey) {    //根据行键读取新闻 返回对应新闻
        ResultData resultData;

        try {
            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, rowKey);
            for (Result ress : res) {

                JsonObject jsonObject = HBaseUtils.jsonObjectTool(ress);
                array.add(jsonObject);

            }
            resultData = ResultData.buildSuccessFromData(array);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }
        
        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<JsonArray>> getNewsByTitle(String newTitle) {   //根据标题读取新闻  返回对应新闻
        ResultData resultData;
        try {
            JsonArray array = new JsonArray();
            hBaseBasicService.RowFilter(HBaseUtils.TABLE_NAME, newTitle);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, newTitle);
            for (Result ress : res) {

                JsonObject jsonObject = HBaseUtils.jsonObjectTool(ress);
                array.add(jsonObject);
            }

            resultData = ResultData.buildSuccessFromData(array);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);

    }

    @Override
    public Mono<ResultData<JsonArray>> getNewsByType(String newType) {     //根据分类读新闻
        ResultData resultData;
        JsonArray array = new JsonArray();

        try {
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, newType);
            for (Result ress : res) {

                JsonObject jsonObject = HBaseUtils.jsonObjectTool(ress);
                array.add(jsonObject);
            }

            resultData = ResultData.buildSuccessFromData(array);
        } catch (Exception e) {
            resultData = ResultData.buildErrorFromData(e);
        }

        return Mono.just(resultData);
    }

    @Override
    public Mono<ResultData<JsonArray>> getNewsByAuthor(String author) {    //根据作者读新闻    返回对应新闻
        ResultData resultData;
        JsonArray array = new JsonArray();

        try {
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, author);
            for (Result ress : res) {

                JsonObject jsonObject = HBaseUtils.jsonObjectTool(ress);
                array.add(jsonObject);
            }
            resultData = ResultData.buildSuccessFromData(array);

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
            String id = HBaseUtils.getGoodId();
            boolean a = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_author, news.getAuthor());
            boolean b = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newType, news.getNewType());
            boolean c = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newTitle, news.getNewTitle());
            boolean d = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_text, news.getText());
            flag = a && b && c && d;
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
