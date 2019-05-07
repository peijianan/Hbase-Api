package com.tensor.api.org;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liaochuntao
 */
@Slf4j
@SpringBootApplication
@EnableNacosDiscovery(globalProperties = @NacosProperties(serverAddr = "192.168.31.217:8848"))
public class HBaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HBaseApiApplication.class);
    }

}
