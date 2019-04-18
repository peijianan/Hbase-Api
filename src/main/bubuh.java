package hbase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.swing.text.TabExpander;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.record.compiler.JBoolean;
import org.apache.hadoop.util.GSet;
import org.mortbay.util.ajax.JSON;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.CellUtil;
import com.google.common.collect.Table.Cell;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.hbase.client.Result;

public interface HBaseBasicService {
	 
	
	//1.根据传入的表名 行键 列族名 列名 返回数据 
	Result getdata(String tableName,String rowkey,String cf,String column) throws Exception; 
	//2.根据传入的 表名 行键 列族名 列名 将数据插入数据库 返回是否成功
	boolean putdata(String tableName,String rowkey,String cf,String column,String value)throws Exception;
	//3.根据传入的 表名 行键 列族名 列名 删除数据 返回是否成功
	boolean delData(String tableName,String rowkey,String cf,String column)throws Exception;
	//4.根据传入的 表名 行键 删除某一行数据 返回是否成功
	boolean delRow(String tableName,String rowkey)throws Exception;
	//5.根据传入的表名 行键  列族 删除某一列族
	boolean delfamily(String tableName,String rowkey,String cf)throws Exception;
    //6.根据传入的表名 扫描整张表
	ResultScanner scantable(String tableName)throws Exception;
	//7.根据传入的表名 起始行键 终止行键 进行扫描
	ResultScanner scantable(String tableName ,String startrowkey ,String stoprowkey)throws Exception;
	//8.根据传入的表名 起始行键 进行扫描
	ResultScanner scantable(String tableName ,String startrowkey)throws Exception;
	 //9.统计行数
	 long count(String tableName)throws Exception;
	 //10.根据传入的表名 列族名 查找符合条件的列
	 ResultScanner FamilyFilter(String tableName,String cf)throws Exception;
	 //11.根据 传入的表名  列名 查找符合条件条件的列 
	 ResultScanner  QualifierFilter(String tableName ,String colmun)throws Exception;
	 //12.根据传入的 表名 行键名 返回符合的行键 返回 该行数据
	 ResultScanner RowFilter(String tableName,String row )throws Exception;
	 //13.取回rowkey以指定prefix开头的所有行 返回 这些行的全部数据
	 ResultScanner PrefixFilter(String tableName, String reg)throws Exception;
	 //14.根据整行中的每个列来做过滤，只要存在一列不满足条件，整行都被过滤掉。返回符合行的全部数据
	 ResultScanner SkipFilter(String tableName,String column)throws Exception;
	 //15.按时间戳检索 返回符合行的全部数据
	 ResultScanner TimestampsFilter(String tableName,List<Long>Timestamps )throws Exception;
	 ResultScanner TimestampsFilter(String tableName,Long Timestamps )throws Exception;
	  //16.一旦遇到一条符合过滤条件的数据，就停止扫描  返回符合行的全部数据
	      //根据行
	 ResultScanner WhileMatchbyrowkeyFilter(String tableName,String rowkey)throws Exception;
	      //根据列
	 ResultScanner  WhileMatchbycolumnFilter(String tableName,String column)throws Exception;
	      //根据值
	 ResultScanner  WhileMatchbyvalueFilter(String tableName ,String value)throws Exception;
	 //17.根据value值寻找 全部列的value都会被检索
	 ResultScanner  ValueFilter(String tableName,String value) throws Exception;
	
}
