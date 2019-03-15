package com.tensor.api.org.router;

import com.tensor.api.org.util.StringConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author liaochuntao
 */
@Configuration
public class StoreRouter {

    @Bean(value = "StoreRouter")
    public RouterFunction StoreRouter() {
        return route(
                POST(StringConst.API + "new/add"),request -> ok().body(BodyInserters.fromObject("info")))
                .andRoute(GET(StringConst.API + "new/get"), request -> ok().body(BodyInserters.fromObject("user")));
    }

}
