//package com.tensor.nacos.api.handler;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//import java.util.function.Supplier;
//
///**
// * @author liaochuntao
// */
//@Slf4j
//@Component
//public class MessagePublish {
//
//    @Value("${mq.host}")
//    private String mqHost;
//
//    @Value("${mq.port}")
//    private int port;
//
//    private Channel channel;
//    private final Supplier<Channel> supplier = () -> {
//        if (channel == null) {
//            synchronized (this) {
//                if (channel == null) {
//                    try {
//                        channel = doOpen();
//                    } catch (InterruptedException e) {
//                        log.error("[Tensor-COSUMER ERROR] : {}", e.getMessage());
//                    }
//                }
//            }
//        }
//        return channel;
//    };
//
//    public MessagePublish() {}
//
//    void exec(String body) {
//        supplier.get().writeAndFlush(body);
//    }
//
//    private class Handler extends SimpleChannelInboundHandler<String> {
//
//        @Override
//        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            super.channelActive(ctx);
//        }
//
//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//            String[] respMsg = msg.split(":");
//            log.info(msg);
//        }
//    }
//
//    private Channel doOpen() throws InterruptedException {
//        EventLoopGroup group = new NioEventLoopGroup();
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
//                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
//                .handler(new LoggingHandler(LogLevel.INFO))
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) {
//                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
//                        ch.pipeline().addLast(new StringDecoder());
//                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new Handler());
//                    }
//                });
//        ChannelFuture future = bootstrap.connect(new InetSocketAddress(mqHost, port)).sync();
//        return future.sync().channel();
//    }
//}
