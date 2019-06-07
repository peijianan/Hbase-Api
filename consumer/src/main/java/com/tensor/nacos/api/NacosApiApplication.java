package com.tensor.nacos.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liaochuntao
 */
@SpringBootApplication
public class NacosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosApiApplication.class, args);
    }

}
