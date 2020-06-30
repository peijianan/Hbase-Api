package com.tensor.api.org.service.spark.base;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

/**
 * 通用功能接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface BaseSparkService {
    //todo :留待添加接口方法

    /**
     * 打印PairRdd中数据
     * @param rdd 数据集
     * @param <K> 键类型
     * @param <V> 值类型
     */
    default <K , V>void showMe(JavaPairRDD<K , V> rdd){
        rdd.foreach((VoidFunction<Tuple2<K, V>>) kvTuple2 -> System.out.println(kvTuple2._1 + " , " + kvTuple2._2));
    }
    /**
     * 打印Rdd中数据
     * @param rdd 数据集
     * @param <T> 数据类型
     */
    default <T>void showMe(JavaRDD<T> rdd){
        rdd.foreach((VoidFunction<T>) System.out::println);
    }
}
