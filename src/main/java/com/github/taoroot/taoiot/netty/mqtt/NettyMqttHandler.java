package com.github.taoroot.taoiot.netty.mqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author : taoroot
 * Date: 2019/9/12
 */
@Log4j2
@AllArgsConstructor
public class NettyMqttHandler extends SimpleChannelInboundHandler<MqttMessage> {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
        MqttHandler<? extends MqttMessage> handler = MqttHandlerProcessor.getHandler(msg.getClass());
        if (handler == null) {
            log.error("no support " + msg.getClass().getTypeName());
            return;
        }
        handler.process0(ctx.channel(), msg);
    }
}
