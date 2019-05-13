package com.tensor.nacos.api.router;

import com.tensor.nacos.api.handler.NacosHandler;
import com.tensor.nacos.api.util.StringConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author liaochuntao
 */
@Configuration
public class NacosRouter {

    @Autowired
    private NacosHandler nacosHandler;

    @Bean(value = "NacosRouter")
    public RouterFunction NacosRouter() {
        return route(
                POST(StringConst.API).and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8))
                        , nacosHandler::post)
                .andRoute(PUT(StringConst.API).and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8))
                        , nacosHandler::put)
                .andRoute(GET(StringConst.API).and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8))
                        , nacosHandler::get);
    }

}
