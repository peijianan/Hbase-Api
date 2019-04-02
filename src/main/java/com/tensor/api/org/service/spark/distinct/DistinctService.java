package com.tensor.api.org.service.spark.distinct;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

/**
 * 去重服务接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface DistinctService {
    /**todo 类型待定
     *
     * @param data 词频与文章数据
     * @param <K> 键，暂定文章id
     * @param <V> 值，文章词列表
     * @return 去重后的文章
     */
    <K , V> JavaRDD<V> distinct(JavaPairRDD<K , V> data);
}
