package com.tensor.api.org.handler.impl;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.handler.NewsHandler;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import com.tensor.api.org.util.alog.SimHashAlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:05
 */
@Component
public class NewsHandlerImpl implements NewsHandler {

    @Autowired
    private HBaseNewsService hBaseNewsService;

    @Override
    public Mono<ServerResponse> saveNew(ServerRequest request) {
        return request.bodyToMono(News.class).map(newsBean -> {
            newsBean.setHashCode(SimHashAlogUtil.simHash(newsBean.getText()));
            return newsBean;
        }).flatMap(newsBean -> {
            Mono<ResultData> mono = Mono.just(hBaseNewsService.putNews(newsBean));
            return ok().body(BodyInserters.fromPublisher(mono.publishOn(Schedulers.elastic()), ResultData.class))
                    .subscribeOn(Schedulers.elastic());
        });
    }

    @Override
    public Mono<ServerResponse> query(ServerRequest request) {
        String start = request.queryParam("start").orElseGet(null);
        String offset = request.queryParam("offset").orElse(null);
        Mono result = Mono.justOrEmpty(hBaseNewsService.getAllNews()).subscribeOn(Schedulers.elastic());
        return null;
    }
}
