package com.tensor.api.org.service.mq;


public interface ProducerService {

    void register(String topic, ConsumerService consumerService);

    <T> T publish(T data);

}
