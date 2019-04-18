package com.tensor.api.org.service.spark;

import com.tensor.api.org.enpity.News;

import java.util.List;

/**
 * 对外可见的接口，提供需要spark完成的服务
 *
 * @author LightDance
 * @date 2019/4/3
 */
public interface SparkService {
    /**
     * 文章去重
     * @param news 文章列表
     * @param <T> todo 新闻类么？
     */
    <T> void distinctNews(T news);

    /**
     * 将参数新闻与已有的全部新闻对比，返回相似度最高的新闻列表
     * @param news 指定新闻
     * @return List类型相似新闻列表
     */
    List<News> getSimilarity(News news);
    /**
     * 将参数新闻与已有的全部新闻对比，返回相似度最高的新闻列表
     * @param news1 指定新闻1
     * @param news2 指定新闻2
     * @return 两者的相似度
     */
    int getSimilarityOfTwo(News news1 , News news2);
}
