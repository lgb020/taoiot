package com.github.taoroot.taoiot.netty.mqtt.impl;

import com.github.taoroot.taoiot.netty.NettyUtil;
import com.github.taoroot.taoiot.netty.mqtt.MqttHandler;
import com.github.taoroot.taoiot.netty.mqtt.NettyMqttHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author : zhiyi
 * Date: 2020/5/7
 */
@Log4j2
@Component
public class MqttPublishHandler implements MqttHandler<MqttPublishMessage> {

    @Override
    public void process(Channel channel, MqttPublishMessage msg) {
        byte[] messageBytes = new byte[msg.payload().readableBytes()];
        msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
        // todo 数据处理

        String topic = msg.variableHeader().topicName();
        MqttQoS qos = msg.fixedHeader().qosLevel();

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
