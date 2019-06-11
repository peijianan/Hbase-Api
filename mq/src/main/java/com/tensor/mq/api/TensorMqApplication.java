package com.tensor.mq.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liaochuntao
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TensorMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(TensorMqApplication.class);
    }

}
