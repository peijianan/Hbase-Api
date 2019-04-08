package com.tensor.api.org.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:05
 */
public interface NewsHandler {

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> saveNew(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> query(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryAll(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryAllAuthor(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryAllTitle(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryByTitle(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryByAuthor(ServerRequest request);

    /**
     *
     * @param request
     * @return
     */
    Mono<ServerResponse> queryByType(ServerRequest request);

}
