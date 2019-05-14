package com.tensor.mq.api.handler.impl;

import com.tensor.mq.api.handler.MQProducerHandler;
import com.tensor.mq.api.pojo.Message;
import com.tensor.mq.api.store.MQStore;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liaochuntao
 */
@Slf4j
@Component
public class MQProducerHandlerImpl extends SimpleChannelInboundHandler<String> implements MQProducerHandler {

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
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] reqMsg = msg.split(":");
        if ("create".equals(reqMsg[0])) {
            String id = connectorPool.addConnector(RoleType.PROVIDER, reqMsg[1], ctx.channel());
            ctx.writeAndFlush("success:" + id).addListener(future -> log.info("[Tensor-MQ REGISTER] client id : {}", future.isSuccess()));
        } else if ("delete".equals(reqMsg[0])) {
            String topic = reqMsg[1];
            String id = reqMsg[2];
            connectorPool.removeConnector(RoleType.PROVIDER, topic, id);
        } else if ("publish".equals(reqMsg[0])) {
            String topic = reqMsg[1];
            String context = reqMsg[2];
            executor.submit(new Executor(topic, context));
        } else {
            ctx.writeAndFlush("bad:403");
        }
    }

    private class Executor implements Runnable {

        private String topic;
        private String body;

        Executor(String topic, String body) {
            this.topic = topic;
            this.body = body;
        }

        @Override
        public void run() {
            mqStore.publish(Message.buildMessage(topic, body));
        }
    }
}
