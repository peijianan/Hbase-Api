package com.tensor.mq.api.config;

import com.tensor.mq.api.handler.mq.MQConsumerHandlerImpl;
import com.tensor.mq.api.handler.mq.MQProducerHandlerImpl;
import com.tensor.org.mq.serialize.kryo.KryoDecoder;
import com.tensor.org.mq.serialize.kryo.KryoEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 *
 * @author liaochuntao
 */
@Slf4j
@Component
public class WebSocketConfigure {

    @Autowired
    private MQProducerHandlerImpl mqProducerHandler;

    @Autowired
    private MQConsumerHandlerImpl mqConsumerHandler;

    @Value("${mq.port}")
    private int port;

    @PostConstruct
    public void start() {
        bind(port);
    }

    private void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(new KryoDecoder(10 * 1024 * 1024));
                            ch.pipeline().addLast(new KryoEncoder());
                            ch.pipeline().addLast(mqConsumerHandler);
                            ch.pipeline().addLast(mqProducerHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("[Tensor MQ] : server has started...");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
