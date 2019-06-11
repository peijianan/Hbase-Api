package com.tensor.api.org.service.spark.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 公共父类
 *
 * @author LightDance
 * @date 2019/4/3
 */
public class BaseSparkServiceImpl implements BaseSparkService {

    private static final String DEFAULT_APP_NAME = "defaultAppName";
    private static final String DEFAULT_MASTER = "local";

    private static boolean isInitialed = false;

    protected static SparkConf conf;
    protected static JavaSparkContext sc;

    /**
     * 初始化conf和sc
     */
    public static void init() {
        if (isInitialed) {
            throw new IllegalStateException("已经初始化过");
        } else {
            synchronized (BaseSparkService.class){
                if (isInitialed){
                    return;
                }
                conf = new SparkConf().setAppName(DEFAULT_APP_NAME).setMaster(DEFAULT_MASTER);
                sc = new JavaSparkContext(conf);
                isInitialed = true;
            }
        }
    }


    public SparkConf getConf() {
        if (isInitialed) {
            return conf;
        } else {
            throw new IllegalStateException("未初始化");
        }
    }
}
