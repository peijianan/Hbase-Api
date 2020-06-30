package com.tensor.api.org.util;


import com.tensor.api.org.enpity.ResultData;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author liaochuntao
 */
public final class ResponseRender {

    public static Mono<ServerResponse> render(Mono dataMono) {
        return ok().header("Access-Control-Allow-Origin", "*")
                .body(BodyInserters.fromPublisher(dataMono, ResultData.class))
                .subscribeOn(Schedulers.elastic());
    }

}
