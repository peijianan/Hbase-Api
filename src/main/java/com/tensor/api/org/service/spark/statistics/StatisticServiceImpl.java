package com.tensor.api.org.service.spark.statistics;

import org.apache.spark.api.java.JavaRDD;

/**
 * 统计功能实现类
 *
 * @author LightDance
 * @date 2019/4/3
 */
public class StatisticServiceImpl implements StatisticsService {
    @Override
    public <T> JavaRDD<T> getWordFrequency(T article) {
        return null;
    }
}
