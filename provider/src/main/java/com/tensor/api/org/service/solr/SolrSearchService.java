package com.tensor.api.org.service.solr;

import java.util.ArrayList;
import java.util.Map;

/*
 * 这里返回的是一个list 里面存放了多个map
 * 其中第一个map存放了返回的结果数和开始的分页数之后的若干个map 存放返回的结果
 *形如：
 * [{start=0, Numfound=1}, {id=2019040804, title=[API测试4],
 * text=[API测试4, xiaoming, 第4测试我喜欢打游戏],
 * author=xiaoming,
 * author_s=xiaoming,
 * content_user=这是用来测试分词的测试,
 * _version_=1630250017009696768}}
 *
 *
 * 这里的pagenumber是分页的页数一般情况下填0(即第一页)或分页的偏移量
 * 这里的searchstatement 是自定义的查询语句
 */

public interface SolrSearchService {
    /**

     * 根据传入的参数查询全部新闻 返回list

     *

     * @param pagenumber

     * @return

     */
    ArrayList<Map>SearchAllNews(int pagenumber)throws Exception;

    /**

     * 根据作者查询新闻 返回list

     *

     * @param pagenumber
     @param author
      * @return

     */
    ArrayList<Map>SearchNewsByAuthor(int pagenumber,String author)throws Exception;

    /**

     * 根据标题查询新闻 返回list

     *

     * @param pagenumber
     * @param title

     * @return

     */
    ArrayList<Map>SearchNewsBytitle(int pagenumber,String title)throws Exception;
    /**

     * 根据类型查询新闻 返回list

     *

     * @param type
     @param pagenumber

      * @return

     */
    ArrayList<Map>SearchNewsBytype(int pagenumber,String type)throws Exception;
    /**

     * 根据文章内容查询新闻 返回list

     *

     * @param pagenumber
     * @param  article

     * @return

     */
    ArrayList<Map>SearchNewsBytext(int pagenumber,String article)throws Exception;
    /**

     * 根据文章出版日期查询新闻 返回list

     *

     * @param pagenumber
     @param  publish_date
      * @return

     */
    ArrayList<Map>SearchNewsBypublish_date(int pagenumber,String publish_date)throws Exception;
    /**

     * 根据文章爬取日期查询新闻 返回list

     *

     * @param pagenumber
     @param  download_date
      * @return

     */
    ArrayList<Map>SearchNewsBydownload_date(int pagenumber,String download_date)throws Exception;
    /**

     * 根据文章ID查询新闻 返回list

     *

     * @param id

     * @return

     */
    ArrayList<Map>SearchNewsByid(long id)throws Exception;
    /**

     * 自定义查询返回 list

     *

     * @param pagenumber
     @param searchstatement

      * @return

     */
    ArrayList<Map>UserdefinedSearch(int pagenumber ,String searchstatement)throws Exception;



}
