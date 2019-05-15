package com.tensor.nacos.api.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.TcpClient;

import java.util.Objects;

/**
 * @author liaochuntao
 */
@Configuration
public class HttpServerConfigure {

    @Autowired
    private Environment environment;

    @Bean
    public HttpServer httpServerForService(@Qualifier(value = "NacosRouter") RouterFunction<?> routerFunction) {
        return getHttpServer(routerFunction);
    }

    @LoadBalanced
    @Bean
    public WebClient loadBalancedWebClientBuilder() {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(10))
                                .addHandlerLast(new WriteTimeoutHandler(10)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    /**
     * <pre>
     * 抽取的函数, 实现具体的 HttpServer 部署实现工作
     * </pre>
     * @param routerFunction 路由实例
     * @return
     */
    private HttpServer getHttpServer(RouterFunction<?> routerFunction) {
        HttpHandler handler = RouterFunctions.toHttpHandler(routerFunction);
        ReactorHttpHandlerAdapter httpHandlerAdapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer httpServer = HttpServer.create()
                .host("localhost")
                .port(Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port"))));
        httpServer.handle(httpHandlerAdapter);
        return httpServer;
    }

}

