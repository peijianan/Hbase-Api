package com.tensor.mq.api.handler.mq;

import com.tensor.mq.api.handler.MQProducerHandler;
import com.tensor.mq.api.pojo.Message;
import com.tensor.mq.api.store.MQStore;
import com.tensor.org.mq.common.MQPublisher;
import com.tensor.org.mq.common.MQResponse;
import com.tensor.org.mq.common.OpType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class MQProducerHandlerImpl extends SimpleChannelInboundHandler<MQPublisher> implements MQProducerHandler {

    @Autowired
    private ConnectorPool connectorPool;

    @Autowired
    private MQStore mqStore;

    private AtomicInteger id = new AtomicInteger(0);

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("[Tensor MQ Publisher-" + id.incrementAndGet() + "]");
        return thread;
    });

    @Override
    public void push(Message message) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQPublisher msg) throws Exception {
        if (msg.getOpType() == OpType.SUBSCRIBE_CREATE.getType()) {
            String id = connectorPool.addConnector(RoleType.PROVIDER, msg.getTopic(), ctx.channel());
            msg.setId(id);
            ctx.writeAndFlush(MQResponse.successResponse(200, OpType.SUBSCRIBE_CREATE.getType(), id))
                    .addListener(future -> log.info("[Tensor-MQ REGISTER] client id : {}", future.isSuccess()));
        } else if (msg.getOpType() == OpType.SUBSCRIBE_DELETE.getType()) {
            String topic = msg.getTopic();
            String id = msg.getId();
            connectorPool.removeConnector(RoleType.PROVIDER, topic, id);
        } else if (msg.getOpType() == OpType.SUBSCRIBE_PUBLISH.getType()) {
            String topic = msg.getTopic();
            String context = msg.getContext();
            executor.submit(new PublishWork(topic, context));
            ctx.writeAndFlush(MQResponse.successResponse(200, OpType.SUBSCRIBE_CREATE.getType(), true));
        } else {
            ctx.writeAndFlush(MQResponse.errResponse(403, -1,"非法参数"));
        }
    }

    private class PublishWork implements Runnable {

        private String topic;
        private String body;

        PublishWork(String topic, String body) {
            this.topic = topic;
            this.body = body;
        }

        @Override
        public void run() {
            mqStore.publish(Message.buildMessage(topic, body));
        }
    }
}
