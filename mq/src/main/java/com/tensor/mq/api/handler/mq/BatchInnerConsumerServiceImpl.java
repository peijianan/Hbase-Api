package com.tensor.mq.api.handler.mq;

import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.pojo.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * @author liaochuntao
 */
public class BatchInnerConsumerServiceImpl implements ConsumerService<List<Message>> {

    private InnerConsumerServiceImpl service;

    public BatchInnerConsumerServiceImpl(InnerConsumerServiceImpl service) {
        this.service = service;
    }

    @Override
    public List<Message> onNext(int reqNum) {
        List<Message> result = new LinkedList<>();
        while (reqNum -- >= 0 && !service.cache.isEmpty()) {
            result.add(service.cache.pollFirst());
        }
        return result;
    }

    @Override
    public void onEvent(Message event) throws Exception {
        service.onEvent(event);
    }
}
