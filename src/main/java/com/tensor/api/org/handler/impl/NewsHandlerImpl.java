package com.tensor.api.org.handler.impl;

import com.tensor.api.org.enpity.New;
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
        return request.bodyToMono(New.class).map(newBean -> {
            newBean.setHashCode(SimHashAlogUtil.simHash(newBean.getText()));
            return newBean;
        }).flatMap(newBean -> {
            Mono<ResultData> mono = Mono.just(hBaseNewsService.putNews(newBean));
            return ok().body(BodyInserters.fromPublisher(mono.publishOn(Schedulers.elastic()), ResultData.class))
                    .subscribeOn(Schedulers.elastic());
        });
    }

    @Override
    public Mono<ServerResponse> query(ServerRequest request) {
        return null;
    }
}
