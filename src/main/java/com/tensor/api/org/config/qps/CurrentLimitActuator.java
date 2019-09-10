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

import com.tensor.api.org.annotation.qps.EnableLimitQPS;
import com.tensor.api.org.annotation.qps.LimitRule;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.util.ResponseRender;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
@Slf4j
@Component
@Aspect
public class CurrentLimitActuator {

    private final QPSConfigure qpsConfigure;

    private Map<String, LimitMethod> methodCache = new ConcurrentHashMap<>();

    public CurrentLimitActuator(QPSConfigure qpsConfigure) {
        this.qpsConfigure = qpsConfigure;
    }

    @Pointcut("execution(* com.tensor.api.org.handler..*.*(..))")
    public void urlHandler() {
    }

    @Around("urlHandler()")
    public Object aroundHttpHandler(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        String key = className + "-" + methodName;
        Class<?> classTarget = pjp.getTarget().getClass();
        if (classTarget.isAnnotationPresent(EnableLimitQPS.class)) {
            Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
            Method method = classTarget.getMethod(methodName, par);
            if (method.isAnnotationPresent(LimitRule.class)) {
                methodCache.computeIfAbsent(key, s -> {
                    LimitMethod limitMethod = new LimitMethod();
                    LimitRule rule = method.getAnnotation(LimitRule.class);
                    limitMethod.setLimitRule(rule);
                    return limitMethod;
                });
                LimitMethod limitMethod = methodCache.get(key);
                LimitRule rule = limitMethod.getLimitRule();
                QPSConfigure.LimitRuleEntry entry = qpsConfigure.getLimiterManager().get(rule.resource());
                ResultData data = entry.tryAcquire();
                if (data == null) {
                    return pjp.proceed();
                }
                return ResponseRender.render(Mono.just(data));
            }
        }
        return pjp.proceed();
    }

    static class LimitMethod {

        private LimitRule limitRule;

        public LimitRule getLimitRule() {
            return limitRule;
        }

        public void setLimitRule(LimitRule limitRule) {
            this.limitRule = limitRule;
        }
    }

}
