package com.tensor.api.org.service.hbase;


import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import reactor.core.publisher.Mono;

public interface HBaseNewsService {

    /**
     * 根据传入的参数将新闻插入数据库 返回是否成功
     *
     * @param news
     * @return
     */
    Mono<ResultData<Boolean>> putNews(News news);

    /**
     * 读取全部新闻
     *
     * @return
     */
    Mono<ResultData<JsonObject>> getAllNews();

    /**
     * 读取全部作者 返回 行键-作者
     *
     * @return
     */
    Mono<ResultData<JsonObject>> getAllAuthor();

    /**
     * 读取全部标题 返回 行键-标题
     *
     * @return
     */
    Mono<ResultData<JsonObject>> getAllTitle();

    /**
     * 根据行键读取新闻 返回对应新闻
     *
     * @param rowKey
     * @return
     */
    Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey);

    /**
     * 根据标题读取新闻  返回对应新闻
     *
     * @param newTitle
     * @return
     */
    Mono<ResultData<JsonObject>> getNewsByTitle(String newTitle);

    /**
     * 根据作者读新闻    返回对应新闻
     *
     * @param author
     * @return
     */
    Mono<ResultData<JsonObject>> getNewsByAuthor(String author);

    /**
     * 根据分类读新闻
     *
     * @param newType
     * @return
     */
    Mono<ResultData<JsonObject>> getNewsByType(String newType);
}
