package com.github.taoroot.taoiot.netty.mqtt.impl;

import com.github.taoroot.taoiot.netty.mqtt.MqttHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

/**
 * @author : zhiyi
 * Date: 2020/5/6
 */
@Component
public class MqttSubscribeHandler implements MqttHandler<MqttSubscribeMessage> {
    @Override
    public void process(Channel channel, MqttSubscribeMessage msg) {
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()),
                new MqttSubAckPayload());
        channel.writeAndFlush(subAckMessage);
    }
}
