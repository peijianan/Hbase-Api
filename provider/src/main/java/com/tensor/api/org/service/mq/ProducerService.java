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
     * 批量获取消息
     *
     * @param topic
     * @param processor
     * @param reqNum
     */
    void register(String topic, BatchConsumerService processor, int reqNum);

    /**
     * 消息发布中心
     *
     * @param data
     * @param <T>
     * @return Mono<T>
     */
    <T> Mono<T> publish(T data);

}
