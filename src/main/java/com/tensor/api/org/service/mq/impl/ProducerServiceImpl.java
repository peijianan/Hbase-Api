package com.tensor.api.org.service.mq.impl;

import com.tensor.api.org.config.mq.HBaseMQConfigure;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.mq.BatchConsumerService;
import com.tensor.api.org.service.mq.ConsumerService;
import com.tensor.api.org.service.mq.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author liaochuntao
 */
@Slf4j
@Scope(scopeName = "singleton")
@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private HBaseMQConfigure.HBaseMQ hBaseMQ;

    public ProducerServiceImpl() {}

    @Override
    public void register(String topic, ConsumerService consumerService) {
        hBaseMQ.register(topic, consumerService);
    }

    @Override
    public void register(String topic, BatchConsumerService processor, int reqNum) {
        BatchConsumerProcessor batchConsumerService = new BatchConsumerProcessor(reqNum, processor);
        hBaseMQ.register(topic, batchConsumerService);
    }

    @Override
    public <T> Mono<T> publish(T data) {
        hBaseMQ.publish((Message) data);
        return Mono.justOrEmpty(data);
    }

}
