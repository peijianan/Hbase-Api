// package com.tensor.nacos.api.config;

// import com.alibaba.nacos.api.annotation.NacosInjected;
// import com.alibaba.nacos.api.config.ConfigService;
// import com.alibaba.nacos.api.config.annotation.NacosValue;
// import com.alibaba.nacos.api.config.listener.AbstractListener;
// import com.alibaba.nacos.api.exception.NacosException;
// import com.alibaba.nacos.api.naming.NamingService;
// import com.alibaba.nacos.api.naming.pojo.Instance;
// import com.tensor.nacos.api.pojo.SystemMonitor;
// import com.tensor.nacos.api.util.HttpUrlUtil;
// import com.tensor.nacos.api.util.LoadBlanceUtils;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
// import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
// import org.springframework.cloud.context.config.annotation.RefreshScope;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.env.Environment;
// import org.springframework.http.MediaType;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

// import javax.annotation.PostConstruct;
// import java.util.List;
// import java.util.concurrent.*;

// /**
//  * @author liaochuntao
//  */
// @Slf4j
// @Configuration
// public class NacosConfigure {

//     @Autowired
//     private Environment environment;

//     @Autowired NacosDiscoveryProperties discoveryProperties;

//     @Autowired NacosConfigProperties configProperties;

//     private static ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1
//             , r -> {
//         Thread thread = new Thread(r);
//         thread.setName("com.tensor.nacos.api.service.monitor");
//         return thread;
//     });

//     @PostConstruct
//     private void init() throws NacosException {
// //        executorService.schedule(new Monitor(discoveryProperties.namingServiceInstance()), 10, TimeUnit.SECONDS);
//         configProperties.configServiceInstance().addListener("test", "DEFAULT_GROUP", new AbstractListener() {
//             @Override
//             public void receiveConfigInfo(String configInfo) {
//                 log.info("更改后的信息 {}", configInfo);
//             }
//         });
//     }

//     private class Monitor implements Callable<Void> {

//         private NamingService namingService;

//         Monitor(NamingService namingService) {
//             this.namingService = namingService;
//         }

//         @Override
//         public Void call() {
//             try {
//                 String serviceName = environment.getProperty("serviceName");
//                 List<Instance> instances = namingService.getAllInstances(serviceName);
//                 for (Instance instance : instances) {
//                     String url = HttpUrlUtil.url(instance.getIp(), 5000, "");
//                     Mono.just(WebClient.create(url).get()
//                             .accept(MediaType.APPLICATION_JSON_UTF8)
//                             .retrieve()
//                             .bodyToMono(SystemMonitor.class))
//                             .onErrorResume(throwable -> {
//                                 throwable.printStackTrace();
//                                 return Mono.just(Mono.just(new SystemMonitor()));
//                             })
//                             .subscribe(monitorMono -> monitorMono.subscribe(systemMonitor -> {
//                                 try {
//                                     log.info("service : {}, ip : {} monitor : {}", serviceName, instance.getIp(), systemMonitor);
//                                     instance.setWeight(LoadBlanceUtils.load(systemMonitor));
//                                     namingService.registerInstance(serviceName, instance);
//                                 } catch (NacosException e) {
//                                     e.printStackTrace();
//                                 }
//                             }));
//                 }
//             } catch (NacosException e) {
//                 log.error(e.getErrMsg());
//             } finally {
//                 executorService.schedule(this, 60 * 5, TimeUnit.SECONDS);
//             }
//             return null;
//         }
//     }

// }

