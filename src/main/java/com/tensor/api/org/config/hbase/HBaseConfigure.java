package com.tensor.api.org.config.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;


/**
 * @author liaochuntao
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class HBaseConfigure {

    @Bean(value = "HbaseConnection")
    public Connection connection() throws Exception {
        Configuration conf;
        Connection connection;
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","47.107.174.124,47.107.166.42,119.23.249.129,120.77.223.42");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.client.retries.number", "3");
        connection= ConnectionFactory.createConnection(conf);
        log.info("HBase 连接操作已被执行");
        return connection;
    }

}
