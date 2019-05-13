package com.tensor.nacos.api;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class StrategyTests {

    private Map<String, BiFunction> strategyMap = new HashMap<>();

    @Before
    public void init() {
        strategyMap.put("add", new Add());
        strategyMap.put("sub", new Sub());
    }

    @Test
    public void test() {
        int a = 10;
        int b = 5;
        cul(a, b, "add");
        cul(a, b, "sub");
    }

    private void cul(int a, int b, String label) {
        System.out.println(strategyMap.get(label).apply(a, b));
    }

    private static class Add implements BiFunction<Integer, Integer, Integer> {

        @Override
        public Integer apply(Integer a, Integer b) {
            return a + b;
        }
    }

    private static class Sub implements BiFunction<Integer, Integer, Integer> {

        @Override
        public Integer apply(Integer a, Integer b) {
            return a - b;
        }
    }
}

