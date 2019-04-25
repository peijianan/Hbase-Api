package com.tensor.api.org.router;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.tensor.api.org.handler.NewsHandler;
import com.tensor.api.org.util.StringConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import javax.annotation.PostConstruct;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author liaochuntao
 */
@Configuration
public class NewRouter {

    @Autowired
    private NewsHandler newsHandler;

    @NacosInjected
    private NamingService namingService;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${nacos.discovery.cluster-name}")
    private String clusterName;

    @PostConstruct
    public void registerInstance() throws NacosException {
        namingService.registerInstance(applicationName, "127.0.0.1", serverPort, clusterName);
    }

    @Bean(value = "StoreRouter")
    public RouterFunction StoreRouter() {
        return route(
                POST(StringConst.API + "new/add").and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::saveNew)
                .andRoute(GET(StringConst.API + "new/get").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::query)
                .andRoute(GET(StringConst.API + "new/all").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryAll)
                .andRoute(GET(StringConst.API + "new/author/all").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryAllAuthor)
                .andRoute(GET(StringConst.API + "new/title/all").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryAllTitle)
                .andRoute(GET(StringConst.API + "new/title/{name}").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryByTitle)
                .andRoute(GET(StringConst.API + "new/author/{name}").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryByAuthor)
                .andRoute(GET(StringConst.API + "new/type/{name}").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::queryByType);
    }

}
