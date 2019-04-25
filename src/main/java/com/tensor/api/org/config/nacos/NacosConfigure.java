package com.tensor.api.org.config.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class NacosConfigure {

    @NacosInjected
    private NamingService namingService;

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${nacos.discovery.cluster-name}")
    private String clusterName;

    @Value("${nacos.client.ip}")
    private String ip;

    @PostConstruct
    public void registerInstance() throws NacosException {
        log.info("ip : {}, port : {}, applicationName : {}", ip, serverPort, applicationName);
        namingService.registerInstance(applicationName, ip, serverPort, clusterName);
    }

}
