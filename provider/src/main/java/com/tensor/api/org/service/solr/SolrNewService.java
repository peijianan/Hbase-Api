
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

 Mono<ResultData<JsonObject>> getAllNews(int pagenumber);


 /**

  * 根据id读取新闻 返回对应新闻

  *

  * @param id

  * @return

  */

 Mono<ResultData<JsonObject>> getNewsByRowKey(String id);

 /**

  * 作者  查询

  *

  * @param author
  @param pagenumbwer
   * @return

  */
 Mono<ResultData<JsonObject>> queryNewsByAuthor(int pagenumbwer ,String author);
 /**

  * 标题  查询

  *

  * @param newTitle
  @param pagenumbwer
   * @return

  */
 Mono<ResultData<JsonObject>> queryNewsByTitle(int pagenumbwer,String newTitle);

 /**

  * 分类查询  查询

  *

  * @param newType
  @param pagenumbwer
   * @return

  */
 Mono<ResultData<JsonObject>> queryNewsByType(int pagenumbwer,String newType);

}
