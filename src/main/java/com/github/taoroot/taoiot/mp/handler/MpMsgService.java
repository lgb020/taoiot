package com.github.taoroot.taoiot.mp.handler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Component;

/**
 * @author : zhiyi
 * Date: 2020/5/7
 */
@Log4j2
@Component
@AllArgsConstructor
public class MpMsgService {

    private final WxMpService wxMpService;
    /**
     * 客服消息
     */
    void sendMsg(String msg) {

    }

    /**
     * 模板消息
     */
    public void sendTemplateMsg(String openId, String deviceName, String deviceInfo, String msgType, String createTime, String remark) {
        WxMpTemplateMessage tsMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId("HjUvN0HogQAPVfvN9b-Yyq_rC6SbxzTM7f7Eq9p746s")
                .build();
        tsMessage.addData(new WxMpTemplateData("keyword1", deviceName));
        tsMessage.addData(new WxMpTemplateData("keyword2", deviceInfo));
        tsMessage.addData(new WxMpTemplateData("keyword3", msgType));
        tsMessage.addData(new WxMpTemplateData("keyword4", createTime));
        tsMessage.addData(new WxMpTemplateData("remark", remark));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(tsMessage);
        } catch (WxErrorException e) {
            log.error("打印验证码推送报错： {}", e);
        }
    }
}
