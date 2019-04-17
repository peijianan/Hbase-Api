package com.tensor.api.org.service.hbase.impl;

import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service(value = "HBaseNewsService")
public class HBaseNewsServiceImpl implements HBaseNewsService {

    @Autowired private HBaseBasicService hBaseBasicService;

    @Override
    public Mono<ResultData<Boolean>> putNews(News news) {
        Mono<ResultData<Boolean>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllNews() {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllAuthor() {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllTitle() {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return Mono.empty();
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByTitle(String newTitle) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByAuthor(String author) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByType(String newType) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        return dataMono;
    }
}
