package com.tensor.api.org.handler.impl;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.handler.NewsHandler;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import com.tensor.api.org.service.mq.ProducerService;
import com.tensor.api.org.service.solr.SolrNewService;
import com.tensor.api.org.util.MessageUtils;
import com.tensor.api.org.util.ResponseRender;
import com.tensor.api.org.util.alog.SimHashAlogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:05
 */
@Component
public class NewsHandlerImpl implements NewsHandler {

    @Autowired
    private HBaseNewsService hBaseNewsService;

    @Autowired
    private ProducerService producerService;

    @Override
    public Mono<ServerResponse> saveNew(ServerRequest request) {
        return request.bodyToMono(News.class)
                .map(SimHashAlogUtils::nlp)
                .map(newBean -> Message.buildMessage(MessageUtils.MQ_TOPIC_STORE, newBean))
                .map(message -> {
                    Mono<Message> messageMono = producerService.publish(message);
                    return messageMono.map(ResultData::buildSuccessFromData);
                })
                .flatMap(ResponseRender::render)
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<ServerResponse> query(ServerRequest request) {
        String start = request.queryParam("start").orElseGet(null);
        String offset = request.queryParam("offset").orElse(null);
        return ResponseRender.render(hBaseNewsService.getAllNews());
    }

    @Override
    public Mono<ServerResponse> queryAll(ServerRequest request) {
        return ResponseRender.render(hBaseNewsService.getAllNews());
    }

    @Override
    public Mono<ServerResponse> queryAllAuthor(ServerRequest request) {
        return ResponseRender.render(hBaseNewsService.getAllAuthor());
    }

    @Override
    public Mono<ServerResponse> queryAllTitle(ServerRequest request) {
        return ResponseRender.render(hBaseNewsService.getAllTitle());
    }

    @Override
    public Mono<ServerResponse> queryByTitle(ServerRequest request) {
        String title = request.pathVariable("name");
        return ResponseRender.render(hBaseNewsService.getNewsByTitle(title));
    }

    @Override
    public Mono<ServerResponse> queryByAuthor(ServerRequest request) {
        String author = request.pathVariable("name");
        return ResponseRender.render(hBaseNewsService.getNewsByAuthor(author));
    }

    @Override
    public Mono<ServerResponse> queryByType(ServerRequest request) {
        String type = request.pathVariable("name");
        return ResponseRender.render(hBaseNewsService.getNewsByType(type));
    }
}
