package com.tensor.api.org.service.hbase.impl;

import com.tensor.api.org.service.hbase.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HBaseBasicServiceImpl implements HBaseBasicService {

    @Autowired
    private Connection connection;

    @Override
    public Result getdata(String tableName, String rowkey, String cf, String cloumn) throws Exception {
        //按照rowkey 获取数据
        Table tName = connection.getTable(TableName.valueOf(tableName)); //指定表名
        try {
            Get get = new Get(Bytes.toBytes(rowkey)); //指定行键
            get.addColumn(cf.getBytes(), cloumn.getBytes());//指定列
            Result result = tName.get(get);
            return result;
        } catch (Exception e) {
            Result result = null;
            System.out.println(e.getMessage());
            return result;
        } finally {
            tName.close();
        }

    }

    @Override
    public boolean putdata(String tableName, String rowkey, String cf, String cloumn, String value) throws Exception {
        //存数据
        Table tName = connection.getTable(TableName.valueOf(tableName));
        try {
            org.apache.hadoop.hbase.client.Put put = new org.apache.hadoop.hbase.client.Put(Bytes.toBytes(rowkey));
            put.add(cf.getBytes(), cloumn.getBytes(), value.getBytes());
            tName.put(put);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            tName.close();
        }

    }

    @Override
    public boolean delData(String tableName, String rowkey, String cf, String cloumn) throws Exception {
        //删数据
        Table tName = connection.getTable(TableName.valueOf(tableName));
        try {
            Delete delete = new Delete(rowkey.getBytes());
            delete.deleteColumn(cf.getBytes(), cloumn.getBytes());
            tName.delete(delete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            tName.close();
        }

    }

    @Override
    public boolean delRow(String tableName, String rowkey) throws Exception {
        //删除一整行
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Delete delete = new Delete(Bytes.toBytes(rowkey));
            table.delete(delete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            table.close();
        }
    }

    @Override
    public boolean delfamily(String tableName, String rowkey, String cf) throws Exception {
        //删除某一列
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Delete delete = new Delete(rowkey.getBytes());
            delete.deleteFamily(cf.getBytes());
            table.delete(delete);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            table.close();
        }

    }

    @Override
    public ResultScanner scantable(String tableName) throws Exception {
        //扫描整张表
        TableName tName = TableName.valueOf(tableName);
        HTable scTable = (HTable) connection.getTable(tName);
        try {
            Scan scan = new Scan();
            ResultScanner rs = ((HTable) scTable).getScanner(scan);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            scTable.close();
        }
    }

    @Override
    public ResultScanner scantable(String tableName, String startrowkey, String stoprowkey) throws Exception {
        //根据起始行和停止行扫描
        TableName tName = TableName.valueOf(tableName);
        Scan scan = new Scan(startrowkey.getBytes(), stoprowkey.getBytes());
        HTable scTable = (HTable) connection.getTable(tName);
        try {
            ResultScanner rs = ((HTable) scTable).getScanner(scan);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            scTable.close();
        }
    }

    @Override
    public ResultScanner scantable(String tableName, String startrowkey) throws Exception {
        //根据起始行键扫描至表尾
        TableName tName = TableName.valueOf(tableName);
        HTable scTable = (HTable) connection.getTable(tName);
        try {
            Scan scan = new Scan(startrowkey.getBytes());
            ResultScanner rs = ((HTable) scTable).getScanner(scan);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            scTable.close();
        }
    }

    @Override
    public long count(String tableName) throws Exception {
        //统计行数
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        long rowCount = 0;
        scan.setFilter(new FirstKeyOnlyFilter());
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            rowCount += result.size();
        }
        return rowCount;
    }

    @Override
    public ResultScanner FamilyFilter(String tableName, String cf) throws Exception {
        //列族过滤器
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            FamilyFilter filter1 = new FamilyFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(cf)));
            scan.setFilter(filter1);
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }
    }

    @Override
    public ResultScanner QualifierFilter(String tableName, String column) throws Exception {
        //列过滤器
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            QualifierFilter filter = new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(column)));
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }
    }

    @Override
    public ResultScanner RowFilter(String tableName, String row) throws Exception {
        //行键过滤器
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            RowFilter filter = new RowFilter(CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes(row)));
            scan.setFilter(filter);
            return table.getScanner(scan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            table.close();
        }
    }

    @Override
    public ResultScanner PrefixFilter(String tableName, String reg) throws Exception {

        Table table = connection.getTable(TableName.valueOf(tableName));

        try {
            Scan scan = new Scan();

            PrefixFilter filter = new PrefixFilter(Bytes.toBytes(reg));
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }
    }

    @Override
    public ResultScanner SkipFilter(String tableName, String cloumn) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));

        try {
            Scan scan = new Scan();
            Filter filter = new SkipFilter(new ValueFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("102"))));
            //Filter filter = new SkipFilter(new DependentColumnFilter(Bytes.toBytes("course"), Bytes.toBytes("art"),false,CompareOp.NOT_EQUAL,new BinaryComparator(Bytes.toBytes("90"))));
            //该过滤器需要配合其他过滤器来使用
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }

    }

    @Override
    public ResultScanner TimestampsFilter(String tableName, List<Long> Timestamps) throws Exception {
        //时间戳
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new TimestampsFilter(Timestamps);
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    @Override
    public ResultScanner TimestampsFilter(String tableName, Long Timestamps) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            //ls中存放所有需要查找匹配的时间戳
            List<Long> ls = new ArrayList<Long>();
            ls.add(Timestamps);
            //java语言的整型常量默认为int型，声明long型常量可以后加”l“或”L“
            Filter filter = new TimestampsFilter(ls);
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }
    }

    @Override
    public ResultScanner WhileMatchbyrowkeyFilter(String tableName, String rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new WhileMatchFilter(new RowFilter(CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes(rowkey))));
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    @Override
    public ResultScanner WhileMatchbycolumnFilter(String tableName, String column) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new WhileMatchFilter(new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(column))));
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        return scanner;
    }

    @Override
    public ResultScanner WhileMatchbyvalueFilter(String tableName, String value) throws Exception {

        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new WhileMatchFilter(new ValueFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes(value))));
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        return scanner;

    }

    @Override
    public ResultScanner ValueFilter(String tableName, String value) throws Exception {
        //值过滤器
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Scan scan = new Scan();
            ValueFilter filter = new ValueFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(value)));
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return scanner;
        } catch (Exception e) {
            e.printStackTrace();
            ResultScanner rs = null;
            return rs;
        } finally {
            table.close();
        }
    }
}
