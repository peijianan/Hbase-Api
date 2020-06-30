/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tensor.api.org.config.qps;

import com.google.common.util.concurrent.RateLimiter;
import com.tensor.api.org.annotation.qps.EnableLimitQPS;
import com.tensor.api.org.annotation.qps.FailStrategy;
import com.tensor.api.org.annotation.qps.LimitRule;
import com.tensor.api.org.enpity.ResultData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
@Component
public class QPSConfigure implements BeanPostProcessor {

    private final Map<String, LimitRuleEntry> limiterManager = new HashMap<>();

    Map<String, LimitRuleEntry> getLimiterManager() {
        return limiterManager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> cls = bean.getClass();
        if (cls.isAnnotationPresent(EnableLimitQPS.class)) {
            Method[] methods = cls.getMethods();
            for (Method method : methods) {
                LimitRule rule = method.getAnnotation(LimitRule.class);
                if (rule != null) {
                    Supplier<LimitRuleEntry> limiterSupplier = () -> {
                        RateLimiter limiter = RateLimiter.create(rule.qps());
                        Class<? extends FailStrategy> failStrategy = rule.failStrategy();
                        try {
                            FailStrategy strategy = failStrategy.newInstance();
                            LimitRuleEntry entry = new LimitRuleEntry();
                            entry.setRateLimiter(limiter);
                            entry.setStrategy(strategy);
                            return entry;
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    };
                    limiterManager.computeIfAbsent(rule.resource(), s -> limiterSupplier.get());
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    static class LimitRuleEntry {

        private FailStrategy strategy;
        private RateLimiter rateLimiter;

        void setStrategy(FailStrategy strategy) {
            this.strategy = strategy;
        }

        void setRateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
        }

        ResultData tryAcquire() {
            return rateLimiter.tryAcquire() ? null : strategy.onLimit();
        }
    }
}
