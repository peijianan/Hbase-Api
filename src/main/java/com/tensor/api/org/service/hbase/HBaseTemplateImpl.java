package com.tensor.api.org.service.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class HBaseTemplateImpl implements HBaseTemplate {

    @Autowired
    private Connection connection;

    public Session getSession(String tableName) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            return Session.builder().table(table).build();
        } catch (IOException e) {
            log.error("获取Hbase Table失败", e);
            return null;
        }
    }


}
