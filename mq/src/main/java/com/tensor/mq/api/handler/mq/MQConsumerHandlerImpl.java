package com.tensor.mq.api.handler.mq;

import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.handler.MQConsumerHandler;
import com.tensor.mq.api.handler.failover.AckPool;
import com.tensor.mq.api.pojo.Message;
import com.tensor.mq.api.store.MQStore;
import com.tensor.mq.api.util.GsonUtils;
import com.tensor.mq.api.util.IDUtils;
import com.tensor.org.mq.common.MQConsumer;
import com.tensor.org.mq.common.OpType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class MQConsumerHandlerImpl extends SimpleChannelInboundHandler<MQConsumer> implements MQConsumerHandler {

    @Autowired
    private ConnectorPool connectorPool;

    @Autowired
    private MQStore mqStore;

    @Autowired
    private AckPool ackPool;

    private AtomicInteger id = new AtomicInteger(0);

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("[Tensor MQ Subscriber-" + id.incrementAndGet() + "]");
        return thread;
    });

    @Override
    public void deregister(String topic, String id) {
        log.info("topic : {}", topic);
        connectorPool.removeConnector(RoleType.CONSUMER, topic, id);
    }

    @Override
    public String register(String topic, Channel channel) {
        log.info("topic : {}", topic);
        return connectorPool.addConnector(RoleType.CONSUMER, topic, channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQConsumer msg) throws Exception {
        if (msg.getOpType() == OpType.CONSUMER_SUBSCRIBE.getType()) {
            String id = register(msg.getTopic(), ctx.channel());
            ctx.writeAndFlush("success:" + id).addListener(future -> log.info("[Tensor-MQ REGISTER] client id : {}", future.isSuccess()));
        } else if (msg.getOpType() == OpType.CONSUMER_UN_SCBSCRIBE.getType()) {
            String topic = msg.getTopic();
            String id = msg.getId();
            deregister(topic, id);
        } else if (msg.getOpType() == OpType.CONSUMER_REQUEST.getType()) {
            String topic = msg.getTopic();
            int reqN = msg.getReqN();
            executor.submit(new ConsumerWork(ctx.channel(), topic, reqN));
        } else if (msg.getOpType() == OpType.CONSUMER_ACK.getType()) {
            ackPool.clientAck(msg.getAckN());
        } else {
            ctx.writeAndFlush("bad:403");
        }
    }

    private class ConsumerWork implements Runnable {

        private Channel session;
        private String topic;
        private int reqN;

        ConsumerWork(Channel session, String topic, int reqN) {
            this.session = session;
            this.topic = topic;
            this.reqN = reqN;
        }

        @Override
        public void run() {
            MQConsumer consumer = new MQConsumer();
            ConsumerService consumerService = mqStore.getService(topic);
            List<Message> result = ((BatchInnerConsumerServiceImpl) consumerService).onNext(reqN);
            List<String> contexts = new LinkedList<>();
            for (Message message : result) {
                contexts.add(String.valueOf(message.getData()));
            }
            consumer.setContexts(contexts);
            consumer.setAckN(IDUtils.uuid());
            ackPool.waitAck(consumer.getAckN(), result);
            session.writeAndFlush(GsonUtils.toJson(consumer));
        }
    }
}
