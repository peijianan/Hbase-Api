package com.tensor.api.org.router;

import com.tensor.api.org.handler.NewsHandler;
import com.tensor.api.org.util.StringConst;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class NewRouter {

    @Autowired
    private NewsHandler newsHandler;

    @Bean(value = "StoreRouter")
    public RouterFunction StoreRouter() {
        return route(
                POST(StringConst.API + "new/add").and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::saveNew)
                .andRoute(GET(StringConst.API + "new/get").and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::query)
                .andRoute(GET(StringConst.API + "new/all").and(accept(MediaType.APPLICATION_JSON_UTF8)),
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
