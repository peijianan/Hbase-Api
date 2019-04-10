package com.tensor.api.org.service.spark.statistics;

import com.tensor.api.org.enpity.News;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

/**
 * 统计功能接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface StatisticsService {
    /**
     * 获取词频
     *
     * @param article 文章
     * @return 词频统计结果rdd
     */
    JavaPairRDD<String , Integer> getWordFrequency(News article);
}
