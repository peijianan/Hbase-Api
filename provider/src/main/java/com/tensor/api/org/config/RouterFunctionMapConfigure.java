package com.tensor.api.org.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.handler.AbstractHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author liaochuntao
 */
@Slf4j
@Configuration
public class RouterFunctionMapConfigure extends AbstractHandlerMapping implements InitializingBean {

    @Nullable
    private RouterFunction<?> routerFunction;

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(this.messageReaders)) {
            ServerCodecConfigurer codecConfigurer = ServerCodecConfigurer.create();
            this.messageReaders = codecConfigurer.getReaders();
        }
        if (this.routerFunction == null) {
            initRouterFunctions();
        }
    }

    private void initRouterFunctions() {
        List<RouterFunction<?>> routerFunctions = routerFunctions();
        this.routerFunction = routerFunctions
                .stream()
                .reduce((routerFunction1, other) -> routerFunction1.andOther(other))
                .orElse(null);
    }

    private List<RouterFunction<?>> routerFunctions() {
        SortedRouterFunctionsContainer container = new SortedRouterFunctionsContainer();
        obtainApplicationContext().getAutowireCapableBeanFactory().autowireBean(container);
        return CollectionUtils.isEmpty(container.routerFunctions) ? Collections.emptyList() : container.routerFunctions;
    }

    @Override
    protected Mono<?> getHandlerInternal(ServerWebExchange exchange) {
        return null;
    }

    private static class SortedRouterFunctionsContainer {

        @Nullable
        private List<RouterFunction<?>> routerFunctions;

        @Autowired(required = false)
        public void setRouterFunctions(List<RouterFunction<?>> routerFunctions) {
            this.routerFunctions = routerFunctions;
        }
    }
}