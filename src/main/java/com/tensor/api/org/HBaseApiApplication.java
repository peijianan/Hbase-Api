package com.tensor.api.org;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author liaochuntao
 */
@Slf4j
@EnableWebFlux
@SpringBootApplication
public class HBaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HBaseApiApplication.class);
    }

}
