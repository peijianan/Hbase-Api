package com.tensor.api.org.service.spark.distinct;

import com.tensor.api.org.enpity.News;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

/**
 * 去重服务接口
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface DistinctService {
    /**
     * 将给定的新闻数据集中，每一条数据 与已有的数据集合比较，有重复则删除
     *
     * @param data 待处理的文章列表
     * @return 去重后的文章列表
     */
    JavaRDD<News> distinct(JavaRDD<News> data);
}
