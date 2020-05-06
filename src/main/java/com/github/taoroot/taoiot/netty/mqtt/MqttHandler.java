package com.github.taoroot.taoiot.netty.mqtt;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author zhiyi
 */
public interface MqttHandler<T extends MqttMessage> {

    void process(Channel channel, T msg);

    @SuppressWarnings("unchecked")
    default void process0(Channel channel, MqttMessage msg) {
        process(channel, (T) msg);
    }
}