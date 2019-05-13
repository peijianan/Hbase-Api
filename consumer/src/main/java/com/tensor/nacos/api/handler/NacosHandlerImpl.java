package com.tensor.nacos.api.handler;

import com.tensor.nacos.api.util.HttpUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class NacosHandlerImpl implements NacosHandler {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Mono<ServerResponse> put(ServerRequest request) {
        return request.bodyToMono(String.class)
                .map(s -> webClientBuilder.build().put()
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
                .map(s -> webClientBuilder.build()
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
        HttpUrlUtil.url(request.uri());
        return Mono.just(HttpUrlUtil.url(request.uri()))
                .map(url -> webClientBuilder
                        .defaultHeaders(httpHeaders -> request.headers().asHttpHeaders())
                        .build()
                        .get()
                        .uri(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }
}
