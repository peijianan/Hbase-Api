package com.tensor.api.org.service.spark.base;

import org.apache.spark.SparkConf;

/**
 * 通用功能接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface BaseSparkService {
    /**
     * 初始化sparkConf和sparkContext
     */
    void init();



}
