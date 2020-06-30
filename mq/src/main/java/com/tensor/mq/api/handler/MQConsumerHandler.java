package com.tensor.mq.api.handler;

import io.netty.channel.Channel;

/**
 * @author liaochuntao
 */
public interface MQConsumerHandler {

    void deregister(String topic, String id);

    String register(String topic, Channel channel);

}
