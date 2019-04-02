package com.tensor.api.org.service.spark;

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
}
