package com.tensor.org.mq.client;

import com.tensor.org.mq.TensorMQProperties;
import com.tensor.org.mq.common.MQConsumer;
import com.tensor.org.mq.common.MQPublisher;
import com.tensor.org.mq.common.OpType;
import com.tensor.org.mq.serialize.kryo.KryoDecoder;
import com.tensor.org.mq.serialize.kryo.KryoEncoder;
import com.tensor.org.mq.service.ConsumerServicce;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class MQClient {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("[Tensor MQ Consumer]");
            thread.setDaemon(true);
            return thread;
        }
    });

    private Channel publisherChannle;
    private Map<String, Channel> channels;
    private ServiceLoader<ConsumerServicce> consumerServiccesSPI;
    private TensorMQProperties properties;
    private MqClientConsumerHandler consumerrHandler;

    public MQClient(TensorMQProperties properties, MqClientConsumerHandler consumerrHandler, MQClientPublisherHandler publisherHandler) {
        try {
            this.publisherChannle = open(properties.getPort(), properties.getIp(), publisherHandler);
            this.properties = properties;
            this.consumerServiccesSPI = ServiceLoader.load(ConsumerServicce.class);
            this.consumerrHandler = consumerrHandler;
            consumerrHandler.setConsumerServiccesSPI(consumerServiccesSPI);
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture publish(MQPublisher publisher) {
        return publisherChannle.writeAndFlush(publisher);
    }

    private Channel open(int port, String host, ChannelHandler ...handlers) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new KryoDecoder(10 * 1024 * 1024));
                        ch.pipeline().addLast(new KryoEncoder());
                        for (ChannelHandler handler : handlers) {
                            ch.pipeline().addLast(handler);
                        }
                    }
                });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
        return future.sync().channel();
    }

    private void init() throws InterruptedException {

        subscribe();

        executorService.scheduleWithFixedDelay(new ConsumerReqWork(consumerServiccesSPI), properties.getReqTime(), properties.getReqTime(), properties.getTimeUnit());

    }

    private void subscribe() throws InterruptedException {
        for (ConsumerServicce consumerServicce : consumerServiccesSPI) {
            Channel channel = open(properties.getPort(), properties.getIp(), consumerrHandler);
            channels.put(consumerServicce.topic(), channel);
            MQConsumer consumer = new MQConsumer();
            consumer.setOpType(OpType.CONSUMER_SUBSCRIBE.getType());
            consumer.setTopic(consumerServicce.topic());
            channel.writeAndFlush(consumer);
        }
    }

    private class ConsumerReqWork implements Runnable {

        private final ServiceLoader<ConsumerServicce> servicces;

        public ConsumerReqWork(ServiceLoader<ConsumerServicce> servicces) {
            this.servicces = servicces;
        }

        @Override
        public void run() {
            onNext();
        }

        void onNext() {
            for (ConsumerServicce servicce : servicces) {
                MQConsumer consumer = new MQConsumer();
                consumer.setReqN(servicce.onNext());
                Channel channel = channels.get(servicce.topic());
                channel.writeAndFlush(consumer);
            }
        }
    }

}
