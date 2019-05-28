package com.tensor.nacos.api.handler;

//import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.tensor.nacos.api.util.HttpUrlUtil;
import com.tensor.nacos.api.util.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author liaochuntao
 */
@Slf4j
@RefreshScope
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
        return request.bodyToMono(Map.class)
                .map(s -> {
                    s.put("id", IDUtils.createID());
                    return s;
                })
                .map(s -> webClient.put()
                        .uri(HttpUrlUtil.url(request.uri()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(s), Map.class)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }

    @Override
    public Mono<ServerResponse> post(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .map(s -> {
                    s.put("id", IDUtils.createID());
                    return s;
                })
                .map(s -> webClient
                        .post()
                        .uri(HttpUrlUtil.url(request.uri()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(Mono.just(s), Map.class)
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(stringMono -> ok().body(stringMono, String.class));
    }

//    @SentinelResource(value = "news-get")
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
        return ok().body(Mono.justOrEmpty(""), String.class);
//        return request.bodyToMono(String.class)
//                .map(s -> {
//                    messagePublish.exec(s);
//                    return Mono.just("OK");
//                }).flatMap(s -> ok().body(s, String.class));
    }
}
