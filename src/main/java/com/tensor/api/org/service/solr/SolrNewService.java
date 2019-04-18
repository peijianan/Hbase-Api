
package com.tensor.api.org.service.solr;

import com.google.gson.JsonObject;

import com.tensor.api.org.enpity.News;

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

    Mono<ResultData<JsonObject>> getAllNews();


    /**

     * 根据行键读取新闻 返回对应新闻

     *

     * @param rowKey

     * @return

     */

    Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey);

    /**

     * 作者  查询

     *

     * @param rowkey

     * @return

     */
    Mono<ResultData<JsonObject>> queryNewsByAuthor(String author);
    /**

     * 标题  查询

     *

     * @param author

     * @return

     */
    Mono<ResultData<JsonObject>> queryNewsByTitle(String newTitle);
    
    /**

     * 分类查询  查询

     *

     * @param newType

     * @return

     */
    Mono<ResultData<JsonObject>> queryNewsByType(String newType);
    
}
