package com.tensor.api.org.service.spark.distinct;

import com.tensor.api.org.enpity.News;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

/**
 * 去重服务实现类
 *
 * @author LightDance
 */
public class DistinctServiceImpl implements DistinctService {

    @Override
    public JavaRDD<News> distinct(JavaRDD<News> data) {
        return null;
    }
}
