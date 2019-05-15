package com.tensor.nacos.api.handler;

import com.tensor.nacos.api.util.HttpUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class NacosHandlerImpl implements NacosHandler {

    @Autowired
    private WebClient webClient;

//    @Autowired
//    private MessagePublish messagePublish;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public Mono<ServerResponse> put(ServerRequest request) {
        return request.bodyToMono(String.class)
                .map(s -> webClient.put()
                        .uri(HttpUrlUtil.url(request.uri()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(s), String.class)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }

    @Override
    public Mono<ServerResponse> post(ServerRequest request) {
        return request.bodyToMono(String.class)
                .map(s -> webClient
                        .post()
                        .uri(HttpUrlUtil.url(request.uri()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(s), String.class)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return Mono.just(HttpUrlUtil.url(request.uri()))
                .map(url -> webClient
                        .get()
                        .uri(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }

    @Override
    public Mono<ServerResponse> publish(ServerRequest request) {
        return ok().build();
//        return request.bodyToMono(String.class)
//                .map(s -> {
//                    messagePublish.exec(s);
//                    return Mono.just("OK");
//                }).flatMap(s -> ok().body(s, String.class));
    }
}
