package com.tensor.api.org.service.mq;


public interface ProducerService {

    <T> T publish(T data);

    void shutDown();

}
