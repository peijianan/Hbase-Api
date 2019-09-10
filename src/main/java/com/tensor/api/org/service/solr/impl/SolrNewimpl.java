package com.tensor.api.org.service.solr.impl;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.Page;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.service.solr.SolrNewService;
import com.tensor.api.org.service.solr.SolrSaveService;
import com.tensor.api.org.service.solr.SolrSearchService;
import com.tensor.api.org.util.ResultCode;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.tensor.api.org.util.hbase.MappingFactory.MAPPING_FACTORY;

/**
 * @author liaochuntao
 */
public class SolrNewimpl implements SolrNewService {

    @Autowired
    private SolrSaveService solrSaveService;
    @Autowired
    private SolrSearchService solrSearchService;

    @Override
    public Mono<ResultData<Boolean>> putNews(News news) {
        ResultData resultData = new ResultData();
        boolean flag;
        try {
            String id = Long.toString(news.getId());
            flag = solrSaveService.putNew(news, id);
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

    @Override
    public Mono<ResultData<Page>> getAllNews(int pagenumber) throws Exception {
        ResultData resultData;
        try {
            ArrayList arr = solrSearchService.SearchAllNews(pagenumber);
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Object ress : arr) {

                News news = MAPPING_FACTORY.mapToObj((Result) ress, News.class);
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
    public Mono<ResultData<Page>> getNewsByRowKey(String id) {
        ResultData resultData;
        try {
            ArrayList arr = solrSearchService.SearchNewsByid(Long.parseLong(id));
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Object ress : arr) {

                News news = MAPPING_FACTORY.mapToObj((Result) ress, News.class);
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
    public Mono<ResultData<Page>> queryNewsByAuthor(int pagenumbwer, String author) {
        ResultData resultData;
        try {
            ArrayList arr = solrSearchService.SearchNewsByAuthor(pagenumbwer, author);
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Object ress : arr) {

                News news = MAPPING_FACTORY.mapToObj((Result) ress, News.class);
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
    public Mono<ResultData<Page>> queryNewsByTitle(int pagenumbwer, String newTitle) {
        ResultData resultData;
        try {
            ArrayList arr = solrSearchService.SearchNewsBytitle(pagenumbwer, newTitle);
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Object ress : arr) {

                News news = MAPPING_FACTORY.mapToObj((Result) ress, News.class);
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
    public Mono<ResultData<Page>> queryNewsByType(int pagenumbwer, String newType) {
        ResultData resultData;
        try {
            ArrayList arr = solrSearchService.SearchNewsBytype(pagenumbwer, newType);
            List<News> newsList = new LinkedList<>();
            Page<List<News>> page = new Page<>();
            for (Object ress : arr) {

                News news = MAPPING_FACTORY.mapToObj((Result) ress, News.class);
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
}
