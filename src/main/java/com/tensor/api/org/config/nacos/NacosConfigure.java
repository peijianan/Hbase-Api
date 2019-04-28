package com.tensor.api.org.config.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author liaochuntao
 */
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

    @Value("${nacos.client-1.ip}")
    private String client1;

    @Value("${nacos.client-2.ip}")
    private String client2;

    @Value("${nacos.client-3.ip}")
    private String client3;

    @Value("${spring.profiles.active}")
    private String env;

    @PostConstruct
    public void registerInstance() throws NacosException {
        if ("dev".equals(env)) {
            log.info("ip : {}, port : {}, applicationName : {}", ip, serverPort, applicationName);
            namingService.registerInstance(applicationName, ip, serverPort, clusterName);
        }
        if ("prod".equals(env)) {
            log.info("ip : {}, port : {}, applicationName : {}", client1, serverPort, applicationName);
            namingService.registerInstance(applicationName, client1, serverPort, clusterName);
            log.info("ip : {}, port : {}, applicationName : {}", client2, serverPort, applicationName);
            namingService.registerInstance(applicationName, client2, serverPort, clusterName);
            log.info("ip : {}, port : {}, applicationName : {}", client3, serverPort, applicationName);
            namingService.registerInstance(applicationName, client3, serverPort, clusterName);
        }
    }

}
