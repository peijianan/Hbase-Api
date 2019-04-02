package com.tensor.api.org.service.spark.statistics;

import org.apache.spark.api.java.JavaRDD;

/**
 * 统计功能接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface StatisticsService {
    /**todo 是否引入request类型
     *
     * 获取词频
     *
     * @param article 文章
     * @param <T> 未确定好的参数类型
     * @return 词频统计结果
     */
    <T> JavaRDD<T> getWordFrequency(T article);
}
