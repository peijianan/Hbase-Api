package com.tensor.api.org.service.spark.similarity;

import com.tensor.api.org.enpity.News;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

/**
 * 相似度服务接口类
 *
 * @author LightDance
 */
public interface SimilarityService {
    /**
     * 两篇文章相似度计算
     * @param article1 文章1
     * @param article2 文章2
     * @return 相似度（海明距离表示）
     */
    int getSimilarityOfTwo(JavaPairRDD<String , Integer> article1 , JavaPairRDD<String , Integer> article2);

    /**
     * 对于给定的文章，给出其与数据库中所有其他文章相似度数据
     * @param article 给定的文章(已做预处理)
     * @return 文章id-海明距离表示的相似度 键值对数据集，越小越相似
     */
    JavaPairRDD<Long , Integer> getSimilarityList(JavaPairRDD<String , Integer> article);
}