
package com.tensor.api.org.service.solr;

import com.google.gson.JsonObject;

import com.tensor.api.org.enpity.News;

import com.tensor.api.org.enpity.ResultData;

import reactor.core.publisher.Mono;

public interface SolrNewService {


    Mono<ResultData<Boolean>> putNews(News news);

    Mono<ResultData<JsonObject>> getAllNews();

    Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey);

    Mono<ResultData<JsonObject>> queryNewsByAuthor(String author);

    Mono<ResultData<JsonObject>> queryNewsByTitle(String newTitle);

    Mono<ResultData<JsonObject>> queryNewsByType(String newType);
    
}
