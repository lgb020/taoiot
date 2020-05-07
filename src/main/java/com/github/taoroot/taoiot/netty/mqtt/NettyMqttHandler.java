package com.github.taoroot.taoiot.netty.mqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : taoroot
 * Date: 2019/9/12
 */
@Log4j2
@AllArgsConstructor
public class NettyMqttHandler extends SimpleChannelInboundHandler<MqttMessage> {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final Map<String, ChannelGroup> TOPICS = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
        MqttHandler<? extends MqttMessage> handler = MqttHandlerProcessor.getHandler(msg.getClass());
        if (handler == null) {
            return;
        }
        handler.process0(ctx.channel(), msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            MqttFixedHeader pingReqFixedHeader =
                    new MqttFixedHeader(MqttMessageType.PINGREQ,
                            false,
                            MqttQoS.AT_MOST_ONCE,
                            false,
                            0);
            ctx.writeAndFlush(new MqttMessage(pingReqFixedHeader));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
