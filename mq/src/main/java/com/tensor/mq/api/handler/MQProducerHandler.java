package com.tensor.mq.api.handler;

import com.tensor.mq.api.pojo.Message;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author liaochuntao
 */
public interface MQProducerHandler {

    void push(Message message);

}
