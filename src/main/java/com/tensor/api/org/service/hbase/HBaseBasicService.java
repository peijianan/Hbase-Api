package com.tensor.api.org.service.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.util.List;

public interface HBaseBasicService {

    //1.根据传入的表名 行键 列族名 列名 返回数据
    Result getdata(String tableName, String rowkey, String cf, String column) throws Exception;

    //2.根据传入的 表名 行键 列族名 列名 将数据插入数据库 返回是否成功
    boolean putdata(String tableName, String rowkey, String cf, String column, String value) throws Exception;

    //3.根据传入的 表名 行键 列族名 列名 删除数据 返回是否成功
    boolean delData(String tableName, String rowkey, String cf, String column) throws Exception;

    //4.根据传入的 表名 行键 删除某一行数据 返回是否成功
    boolean delRow(String tableName, String rowkey) throws Exception;

    //5.根据传入的表名 行键  列族 删除某一列族
    boolean delfamily(String tableName, String rowkey, String cf) throws Exception;

    //6.根据传入的表名 扫描整张表
    ResultScanner scantable(String tableName) throws Exception;

    //7.根据传入的表名 起始行键 终止行键 进行扫描
    ResultScanner scantable(String tableName, String startrowkey, String stoprowkey) throws Exception;

    //8.根据传入的表名 起始行键 进行扫描
    ResultScanner scantable(String tableName, String startrowkey) throws Exception;

    //9.统计行数
    long count(String tableName) throws Exception;

    //10.根据传入的表名 列族名 查找符合条件的列
    ResultScanner FamilyFilter(String tableName, String cf) throws Exception;

    //11.根据 传入的表名  列名 查找符合条件条件的列
    ResultScanner QualifierFilter(String tableName, String colmun) throws Exception;

    //12.根据传入的 表名 行键名 返回符合的行键 返回 该行数据
    ResultScanner RowFilter(String tableName, String row) throws Exception;

    //13.取回rowkey以指定prefix开头的所有行 返回 这些行的全部数据
    ResultScanner PrefixFilter(String tableName, String reg) throws Exception;

    //14.根据整行中的每个列来做过滤，只要存在一列不满足条件，整行都被过滤掉。返回符合行的全部数据
    ResultScanner SkipFilter(String tableName, String column) throws Exception;

    //15.按时间戳检索 返回符合行的全部数据
    ResultScanner TimestampsFilter(String tableName, List<Long> Timestamps) throws Exception;

    ResultScanner TimestampsFilter(String tableName, Long Timestamps) throws Exception;

    //16.一旦遇到一条符合过滤条件的数据，就停止扫描  返回符合行的全部数据
    //根据行
    ResultScanner WhileMatchbyrowkeyFilter(String tableName, String rowKey) throws Exception;

    //根据列
    ResultScanner WhileMatchbycolumnFilter(String tableName, String column) throws Exception;

    //根据值
    ResultScanner WhileMatchbyvalueFilter(String tableName, String value) throws Exception;

    //17.根据value值寻找 全部列的value都会被检索
    ResultScanner ValueFilter(String tableName, String value) throws Exception;


}
