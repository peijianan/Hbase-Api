package com.tensor.api.org.service.hbase;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.Page;
import com.tensor.api.org.enpity.ResultData;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author liaochuntao
 */
public interface HBaseNewsService {


    /**
     * 读取全部新闻
     *
     * @return
     */
    Mono<ResultData<Page>> getAllNews();

    /**
     * 读取全部作者 返回 行键-作者
     *
     * @return
     */
    Mono<ResultData<Page>> getAllAuthor();

    /**
     * 读取全部标题 返回 行键-标题
     *
     * @return
     */
    Mono<ResultData<Page>> getAllTitle();

    /**
     * 根据行键读取新闻 返回对应新闻
     *
     * @param rowKey
     * @return
     */
    Mono<ResultData<Page>> getNewsByRowKey(String rowKey);

    /**
     * 根据标题读取新闻  返回对应新闻
     *
     * @param newTitle
     * @return
     */
    Mono<ResultData<Page>> getNewsByTitle(String newTitle);

    /**
     * 根据作者读新闻    返回对应新闻
     *
     * @param author
     * @return
     */
    Mono<ResultData<Page>> getNewsByAuthor(String author);

    /**
     * 根据分类读新闻
     *
     * @param newType
     * @return
     */
    Mono<ResultData<Page>> getNewsByType(String newType);
}
