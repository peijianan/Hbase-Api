package com.tensor.org.mq.client;

import com.tensor.org.mq.common.MQResponse;
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
public class MQClientPublisherHandler extends SimpleChannelInboundHandler<MQResponse> {

    private ServiceLoader<PublishListener> publishListeners;

    public MQClientPublisherHandler() {
        publishListeners = ServiceLoader.load(PublishListener.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQResponse msg) throws Exception {
        for (PublishListener publishListener : publishListeners) {
            if (Objects.equals(publishListener.topic(), msg.getTopic())) {
                publishListener.onEvent(new PublishEvent(msg));
            }
        }
    }
}
