package com.github.taoroot.taoiot.netty.mqtt.impl;

import com.github.taoroot.taoiot.netty.NettyUtil;
import com.github.taoroot.taoiot.netty.mqtt.MqttHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author : zhiyi
 * Date: 2020/5/7
 */
@Log4j2
@Component
public class MqttDisconnectHandler implements MqttHandler<MqttConnectMessage> {
    @Override
    public void process(Channel channel, MqttConnectMessage msg) {
        channel.close();
        log.debug("DISCONNECT - clientId: {}", NettyUtil.getName(channel));
    }
}
