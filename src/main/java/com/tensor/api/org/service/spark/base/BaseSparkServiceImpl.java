package com.tensor.api.org.service.spark.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 公共父类
 *
 * @author LightDance
 * @date 2019/4/3
 */
public class BaseSparkServiceImpl implements BaseSparkService{

    private static final String DEFAULT_APP_NAME = "defaultAppName";
    private static final String DEFAULT_MASTER = "local";

    private static boolean isInitialed = false;

    protected SparkConf conf;
    protected JavaSparkContext sc;

    @Override
    public void init() {
        if (isInitialed){
            throw new IllegalStateException("已经初始化过");
        }else {
            conf = new SparkConf();
            conf.setAppName(DEFAULT_APP_NAME).setMaster(DEFAULT_MASTER);
        }
    }


    public SparkConf getConf() {
        return conf;
    }
}
