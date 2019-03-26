package com.tensor.api.org.handler.impl;

import com.tensor.api.org.enpity.New;
import com.tensor.api.org.handler.StoreHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:05
 */
@Component
public class StoreHandlerImpl implements StoreHandler {

    @Autowired
    private

    @Override
    public Mono<ServerResponse> saveNew(ServerRequest request) {
        return request.bodyToMono(New.class).map(newBean -> {

        })
    }
}
