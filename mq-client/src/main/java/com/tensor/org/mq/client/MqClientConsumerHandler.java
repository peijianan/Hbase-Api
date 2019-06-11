package com.tensor.org.mq.client;

import com.tensor.org.mq.common.MQConsumer;
import com.tensor.org.mq.service.ConsumerServicce;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
@Component
public class MqClientConsumerHandler extends SimpleChannelInboundHandler<MQConsumer> {

    private ServiceLoader<ConsumerServicce> consumerServiccesSPI;

    public MqClientConsumerHandler() {}

    public void setConsumerServiccesSPI(ServiceLoader<ConsumerServicce> consumerServiccesSPI) {
        this.consumerServiccesSPI = consumerServiccesSPI;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQConsumer msg) throws Exception {
        for (ConsumerServicce consumerServicce : consumerServiccesSPI) {
            if (Objects.equals(consumerServicce.topic(), msg.getTopic())) {
                consumerServicce.onEvent(msg.getContexts());
            }
        }
    }

}
