package com.tensor.api.org.service.hbase;

import org.mortbay.util.ajax.JSON;
import com.google.gson.JsonObject;
import  com.tensor.api.org.enpity.New;
import  com.tensor.api.org.enpity.ResultData;

public interface HBaseNews {
	
	//1.根据传入的参数将新闻插入数据库 返回是否成功
	 ResultData<Boolean>Putnews(New news);
	 //2.读取全部新闻
	 ResultData<JsonObject>GetallNews();
	 //3.读取全部文章 返回行键-文章
	 ResultData<JsonObject>GetallText();
	 //4.读取全部作者 返回 行键-作者
	 ResultData<JsonObject>GetallAuthor();
	 //5.读取全部标题 返回 行键-标题
	 ResultData<JsonObject>GetallTitle();
	 //6.根据行键读取新闻 返回对应新闻
	 ResultData<JsonObject>Getnewstbyrowkey(String tablename,String rowkey);
	 //7.根据标题读取新闻  返回对应新闻
	 ResultData<JsonObject>Getnewsbytitle(String tablename ,String newtitle);
	 //8.根据作者读新闻    返回对应新闻
	 ResultData<JsonObject>Getnewsnyauthor(String tablename,String author);
	 //9.根据分类读新闻
	 ResultData<JsonObject>Getnewsbytype(String tablename,String newType);	
}
