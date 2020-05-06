package com.github.taoroot.taoiot.mp;

import com.github.taoroot.taoiot.netty.mqtt.MqttHandler;
import com.github.taoroot.taoiot.netty.mqtt.NettyMqttHandler;
import com.github.taoroot.taoiot.security.annotation.NotAuth;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * @author zhiyi
 */
@Slf4j
@RestController
@RequestMapping("/mp")
@NotAuth
@AllArgsConstructor
public class MpEndpoint {

    private final WxMpService wxMpService;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String auth(@RequestParam(name = "signature", required = false) String signature,
                       @RequestParam(name = "timestamp", required = false) String timestamp,
                       @RequestParam(name = "nonce", required = false) String nonce,
                       @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("mp auth：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "-1";
    }

    /**
     * 新的消息
     *
     * @param requestBody
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encryptType
     * @param msgSignature
     * @return
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String newMsg(@RequestBody String requestBody,
                         @RequestParam("signature") String signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("openid") String openid,
                         @RequestParam(name = "encrypt_type", required = false) String encryptType,
                         @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        // 验证
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            return "-1";
        }

        // 初始化用户上下文
//        SecurityContextHolder.setContext(null);

        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
        log.info("new msg: {}", inMessage);

        // 文字
        if (inMessage.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
            // 普通用户
            // 开发者用户
            // 1. taoiot:
            // 2. mqtt:
            // 3. 其他
        }
        if (inMessage.getMsgType().equals(WxConsts.XmlMsgType.VOICE)) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttPublishVariableHeader("topic", 0), Unpooled.buffer().writeBytes(inMessage.getRecognition().getBytes()));

            NettyMqttHandler.channels.writeAndFlush(publishMessage);

            return WxMpXmlOutMessage.TEXT().content("发送成功")
                    .fromUser(inMessage.getToUser()).toUser(inMessage.getFromUser())
                    .build().toXml();
        }
        return WxMpXmlOutMessage.TEXT().content("消息有误")
                .fromUser(inMessage.getToUser()).toUser(inMessage.getFromUser())
                .build().toXml();
    }
}
