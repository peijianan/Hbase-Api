package com.tensor.api.org.config;

import com.tensor.api.org.enpity.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

/**
 * @author liaochuntao
 */
@Slf4j
@Configuration
public class GlobalErrorWebExceptionConfigure {

    @Component
    @Order(-2)
    public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

        public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                              ResourceProperties resourceProperties,
                                              ApplicationContext applicationContext,
                                              ServerCodecConfigurer serverCodecConfigurer) {
            super(errorAttributes, resourceProperties, applicationContext);
            super.setMessageWriters(serverCodecConfigurer.getWriters());
            super.setMessageReaders(serverCodecConfigurer.getReaders());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            final Map<String, Object> errorMap = getErrorAttributes(request, true);
            log.error("[请求url信息]：{}", request.uri());
            log.error("[内部错误信息]：{}", errorMap.get("trace"));
            Mono<ResultData> errMono = Mono.just(ResultData.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .msg("内部错误").data(errorMap.get("trace")).build());
            return ServerResponse.ok().body(BodyInserters
                    .fromPublisher(errMono.publishOn(Schedulers.elastic()), ResultData.class))
                    .subscribeOn(Schedulers.elastic());
        }
    }

}