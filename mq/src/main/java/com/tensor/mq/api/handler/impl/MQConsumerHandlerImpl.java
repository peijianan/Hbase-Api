package com.tensor.mq.api.handler.impl;

import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.handler.MQConsumerHandler;
import com.tensor.mq.api.pojo.Message;
import com.tensor.mq.api.store.MQStore;
import com.tensor.mq.api.util.GsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class MQConsumerHandlerImpl extends SimpleChannelInboundHandler<String> implements MQConsumerHandler {

    @Autowired
    private ConnectorPool connectorPool;

    @Autowired
    private MQStore mqStore;

    private AtomicInteger id = new AtomicInteger(0);

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("[Tensor MQ Subscribe-" + id.incrementAndGet() + "]");
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
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] reqMsg = msg.split(":");
        if ("subscribe".equals(reqMsg[0])) {
            String id = register(reqMsg[1], ctx.channel());
            ctx.writeAndFlush("success:" + id).addListener(future -> log.info("[Tensor-MQ REGISTER] client id : {}", future.isSuccess()));
        } else if ("unsubscribe".equals(reqMsg[0])) {
            String topic = reqMsg[1];
            String id = reqMsg[2];
            deregister(topic, id);
        } else if ("request".equals(reqMsg[0])) {
            String topic = reqMsg[1];
            int reqN = Integer.valueOf(reqMsg[2]);
            executor.submit(new Executor(ctx.channel(), topic, reqN));
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private class Executor implements Runnable {

        private Channel session;
        private String topic;
        private int reqN;

        Executor(Channel session, String topic, int reqN) {
            this.session = session;
            this.topic = topic;
            this.reqN = reqN;
        }

        @Override
        public void run() {
            ConsumerService consumerService = mqStore.getService(topic);
            List<Message> result = ((BatchInnerConsumerServiceImpl) consumerService).onNext(reqN);
            session.writeAndFlush(GsonUtils.toJson(result));
        }
    }
}
