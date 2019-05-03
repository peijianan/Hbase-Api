package com.tensor.api.org.service.mq.impl;

import com.lmax.disruptor.dsl.Disruptor;
import com.tensor.api.org.config.mq.HBaseMQConfigure;
import com.tensor.api.org.enpity.mq.Message;
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

    private static Disruptor<Message> MQ = null;

    public ProducerServiceImpl() {}

    @Override
    public void register(String topic, ConsumerService consumerService) {
        hBaseMQ.register(topic, consumerService);
    }

    @Override
    public <T> Mono<T> publish(T data) {
        if (data instanceof Message) {
            MQ.publishEvent(((event, sequence) -> Message.adaper(sequence, event, (Message) data)));
        } else {
            Message m = Message.builder().data(data).publishTime(System.currentTimeMillis()).build();
            MQ.publishEvent((event, sequence) -> Message.adaper(sequence, event, m));
        }
        return Mono.justOrEmpty(data);
    }

}
