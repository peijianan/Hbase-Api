package com.tensor.api.org.service.mq;

import com.lmax.disruptor.WorkHandler;
import com.tensor.api.org.enpity.mq.Message;

/**
 * @author liaochuntao
 */
public interface ConsumerService extends WorkHandler<Message> {

}
