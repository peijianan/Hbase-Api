package com.tensor.api.org.service.solr;

import com.tensor.api.org.enpity.News;

import com.tensor.api.org.enpity.Page;
import com.tensor.api.org.enpity.ResultData;

import reactor.core.publisher.Mono;

public interface SolrNewService {

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
    Mono<ResultData<Page>> getAllNews(int pagenumber) throws Exception;


    /**
     * 根据id读取新闻 返回对应新闻
     *
     * @param id
     * @return
     */
    Mono<ResultData<Page>> getNewsByRowKey(String id);

    /**
     * 作者  查询
     *
     * @param author
     * @param pagenumbwer
     * @return
     */
    Mono<ResultData<Page>> queryNewsByAuthor(int pagenumbwer, String author);

    /**
     * 标题  查询
     *
     * @param newTitle
     * @param pagenumbwer
     * @return
     */
    Mono<ResultData<Page>> queryNewsByTitle(int pagenumbwer, String newTitle);

    /**
     * 分类查询  查询
     *
     * @param newType
     * @param pagenumbwer
     * @return
     */
    Mono<ResultData<Page>> queryNewsByType(int pagenumbwer, String newType);

}
