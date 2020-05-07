package com.github.taoroot.taoiot.netty.mqtt.impl;

import com.github.taoroot.taoiot.mp.handler.MpMsgService;
import com.github.taoroot.taoiot.netty.NettyUtil;
import com.github.taoroot.taoiot.netty.mqtt.MqttHandler;
import com.github.taoroot.taoiot.netty.mqtt.NettyMqttHandler;
import com.github.taoroot.taoiot.security.SecurityUser;
import com.github.taoroot.taoiot.security.SecurityUserDetailsService;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.mqtt.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : zhiyi
 * Date: 2020/5/7
 */
@Log4j2
@Component
@AllArgsConstructor
public class MqttPublishHandler implements MqttHandler<MqttPublishMessage> {

    private final MpMsgService mpMsgService;
    private final SecurityUserDetailsService userDetailsService;

    @SneakyThrows
    @Override
    public void process(Channel channel, MqttPublishMessage msg) {
        byte[] messageBytes = new byte[msg.payload().readableBytes()];
        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
        String context = new String(messageBytes, StandardCharsets.UTF_8);
        // todo 数据处理

        String topic = msg.variableHeader().topicName();
        MqttQoS qos = msg.fixedHeader().qosLevel();
        String userId = topic.split("/")[0];

        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUserId(Integer.valueOf(userId));

        // 特殊处理
        mpMsgService.sendTemplateMsg(
                user.getWxMpOpenid(),
                NettyUtil.getName(channel),
                context,
                "MQTT",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                ""
        );

        ChannelGroup channelGroup = NettyMqttHandler.TOPICS.get(topic);

        if (channelGroup != null) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(
                            MqttMessageType.PUBLISH,
                            false,
                            qos,
                            false,
                            0),
                    new MqttPublishVariableHeader(
                            topic,
                            0),
                    Unpooled.buffer().writeBytes(messageBytes));
            channelGroup.writeAndFlush(publishMessage);
            log.debug("PUBLISH - clientId: {}, topic: {}, Qos: {}, channels: {}",
                    NettyUtil.getName(channel),
                    topic,
                    qos.value(),
                    channelGroup.size()
            );
        }

    }
}
