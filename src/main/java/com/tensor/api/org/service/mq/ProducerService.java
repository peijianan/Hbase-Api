package com.tensor.api.org.service.mq;


import reactor.core.publisher.Mono;

/**
 * @author liaochuntao
 */
public interface ProducerService {

    /**
     * 向消息中心注册某个Topic的消费者
     *
     * @param topic
     * @param consumerService
     */
    void register(String topic, ConsumerService consumerService);

    /**
     * 消息发布中心
     *
     * @param data
     * @param <T>
     * @return Mono<T>
     */
    <T> Mono<T> publish(T data);

}
