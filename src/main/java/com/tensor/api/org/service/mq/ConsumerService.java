package com.tensor.api.org.service.mq;

import com.lmax.disruptor.WorkHandler;
import com.tensor.api.org.enpity.mq.Message;

public interface ConsumerService extends WorkHandler<Message> {
}
