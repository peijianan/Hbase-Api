package com.tensor.api.org.service.hbase;


import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.New;
import com.tensor.api.org.enpity.ResultData;

public interface HBaseNewsService {

    /**
     * 根据传入的参数将新闻插入数据库 返回是否成功
     *
     * @param news
     * @return
     */
    ResultData<Boolean> putNews(New news);

    /**
     * 读取全部新闻
     *
     * @return
     */
    ResultData<JsonObject> getAllNews();

    /**
     * 读取全部文章 返回行键-文章
     *
     * @return
     */
    ResultData<JsonObject> getAllText();

    /**
     * 读取全部作者 返回 行键-作者
     *
     * @return
     */
    ResultData<JsonObject> getAllAuthor();

    /**
     * 读取全部标题 返回 行键-标题
     *
     * @return
     */
    ResultData<JsonObject> getAllTitle();

    /**
     * 根据行键读取新闻 返回对应新闻
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    ResultData<JsonObject> getNewstByRowKey(String tableName, String rowKey);

    /**
     * 根据标题读取新闻  返回对应新闻
     *
     * @param tableName
     * @param newTitle
     * @return
     */
    ResultData<JsonObject> Getnewsbytitle(String tableName, String newTitle);

    /**
     * 根据作者读新闻    返回对应新闻
     *
     * @param tableName
     * @param author
     * @return
     */
    ResultData<JsonObject> Getnewsnyauthor(String tableName, String author);

    /**
     * 根据分类读新闻
     *
     * @param tableName
     * @param newType
     * @return
     */
    ResultData<JsonObject> Getnewsbytype(String tableName, String newType);
}
