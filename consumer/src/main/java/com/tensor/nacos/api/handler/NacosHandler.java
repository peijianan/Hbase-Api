package com.tensor.nacos.api.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author liaochuntao
 */
public interface NacosHandler {

    Mono<ServerResponse> put(ServerRequest request);

    Mono<ServerResponse> post(ServerRequest request);

    Mono<ServerResponse> get(ServerRequest request);

    Mono<ServerResponse> publish(ServerRequest request);

}
