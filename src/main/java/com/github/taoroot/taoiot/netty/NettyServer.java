package com.github.taoroot.taoiot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author zhiyi
 */
@Log4j2
public class NettyServer {

    private EventLoopGroup mainGroup;

    private EventLoopGroup subGroup;

    private EventLoopGroup handlerGroup;

    @Resource
    private NettyProperties nettyProperties;

    /**
     * 启动Netty
     */
    @SneakyThrows
    @PostConstruct
    private void start() {
        mainGroup = new NioEventLoopGroup(1);
        subGroup = new NioEventLoopGroup();
        handlerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(mainGroup, subGroup)
                // Main Reactor
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                // Sub Reactor
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                    }
                });
        serverBootstrap.bind(nettyProperties.getTcpPort()).sync();
        log.info("Netty started on port(s): {} (tcp) ", nettyProperties.getTcpPort());
    }


    /**
     * 关闭Netty
     */
    @SneakyThrows
    private void stop() {
        if (mainGroup != null) {
            mainGroup.shutdownGracefully().sync();
        }
        if (subGroup != null) {
            subGroup.shutdownGracefully().sync();
        }
        if (handlerGroup != null) {
            handlerGroup.shutdownGracefully().sync();
        }
        log.info("Netty stopped");
    }
}