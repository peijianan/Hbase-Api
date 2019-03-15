package com.tensor.api.org.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.netty.http.server.HttpServer;

import java.util.Objects;

/**
 * @author liaochuntao
 */
@Configuration
public class HttpServerConfigure {

    @Autowired
    private Environment environment;

    @Bean
    public HttpServer httpServerForService(@Qualifier(value = "StoreRouter") RouterFunction<?> routerFunction) {
        return getHttpServer(routerFunction);
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
