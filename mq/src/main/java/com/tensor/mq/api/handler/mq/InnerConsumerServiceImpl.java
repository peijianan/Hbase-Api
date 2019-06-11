package com.tensor.mq.api.handler.mq;

import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.pojo.Message;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author liaochuntao
 */
public class InnerConsumerServiceImpl implements ConsumerService<Message> {

    protected final ConcurrentLinkedDeque<Message> cache = new ConcurrentLinkedDeque<Message>();

    @Override
    public void onEvent(Message event) throws Exception {
        cache.add(event);
    }

    @Override
    public Message onNext(int reqNum) {
        return cache.pollFirst();
    }
}
