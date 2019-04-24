package com.tensor.api.org.service.mq.impl;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.mq.ConsumerService;
import com.tensor.api.org.service.mq.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Scope(scopeName = "singleton")
@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private ConsumerService consumerService;

    private static Disruptor<Message> MQ = null;

    public ProducerServiceImpl() {}

    @PostConstruct
    public void init() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        EventFactory<Message> factory = Message::new;
        int ringBufferSize = 1024 * 1024;
        MQ = new Disruptor<>(factory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        MQ.handleEventsWithWorkerPool(consumerService);
        MQ.start();
    }

    @Override
    public <T> T publish(T data) {
        Message m = Message.builder().data(data).publishTime(System.currentTimeMillis()).build();
        MQ.publishEvent((event, sequence) -> Message.adaper(sequence, event, m));
        return data;
    }

    @PreDestroy
    public void shutDown() {
        MQ.shutdown();
    }

}
