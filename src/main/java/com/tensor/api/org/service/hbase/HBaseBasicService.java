package com.tensor.api.org.service.hbase;

import com.google.gson.JsonObject;
import org.apache.hadoop.hbase.client.Result;

public interface HBaseBasicService {
	 
	
	//1.根据传入的表名 行键 列族名 列名 返回数据 
	Result getdata(String tableName,String rowkey,String cf,String cloumn); 
	//2.根据传入的 表名 行键 列族名 列名 将数据插入数据库 返回是否成功
	boolean putdata(String tableName,String rowkey,String cf,String cloumn,String value);
	//3.根据传入的 表名 行键 列族名 列名 删除数据 返回是否成功
	boolean delData(String tableName,String rowkey,String cf,String cloumn);
	//4.根据传入的 表名 行键 删除某一行数据 返回是否成功
	boolean delRow(String tableName,String rowkey);
	//5.根据传入的表名 行键  列族 删除某一列族
	boolean delfamily(String tableName,String rowkey,String cf);
                //6.根据传入的表名 扫描整张表
	 Result scantable(String tableName);
	//7.根据传入的表名 起始行键 终止行键 进行扫描
	 Result scantable(String tableName ,String startrowkey ,String stoprowkey);
	//8.根据传入的表名 起始行键 进行扫描
	 Result scantable(String tableName ,String startrowkey);
	 //9.统计行数
	 long count();
	 //10.根据传入的表名 列族名 查找符合条件的列 返回 行键-列名-列族-数据
	 Result FamilyFilter(String tableName,String cf);
	 //11.根据 传入的表名  列名 查找符合条件条件的列 返回行键-列名-列族-数据
	 Result  QualifierFilter(String tableName ,String clomun);
	 //12.根据传入的 表名 行键名 返回符合的行键 返回 该行数据
	 Result RowFilter(String tableName,String row );
	 //13.取回rowkey以指定prefix开头的所有行 返回 这些行的全部数据
	 Result PrefixFilter(String tableName, String reg);
	 //14.根据整行中的每个列来做过滤，只要存在一列不满足条件，整行都被过滤掉。返回符合行的全部数据
	 Result SkipFilter(String tableName,String cloumn);
	 //15.按时间戳检索 返回符合行的全部数据
	  Result TimestampsFilter(String tableName,List<Long>Timestamps );
	  Result TimestampsFilter(String tableName,Long Timestamps );
	  //16.一旦遇到一条符合过滤条件的数据，就停止扫描  返回符合行的全部数据
	      //根据行
	       Result WhileMatchbyrowkeyFilter(String tableName,String rowkey);
	      //根据列
	       Result  WhileMatchbycolumnFilter(String tableName,String column);
	      //根据值
	      Result  WhileMatchbyvalueFilter(String tableName ,String value);
	  
	
}
