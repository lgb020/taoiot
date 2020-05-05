package com.github.taoroot.taoiot.security;

import cn.hutool.core.util.CharsetUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.taoroot.taoiot.security.service.DbUserDetailsServiceImpl;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author : zhiyi
 * Date: 2020/2/17
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@Import(DbUserDetailsServiceImpl.class)
public class SecurityAutoConfiguration {

    @Resource
    private SecurityProperties securityProperties;

    /**
     * 微信公众号API的Service
     */
    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(securityProperties.getWx().getAppId());
        wxMpConfigStorage.setSecret(securityProperties.getWx().getAppSecret());
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    /**
     * 支付宝客户端API
     */
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                securityProperties.getAli().getAppId(),
                securityProperties.getAli().getPrivateKey(),
                "json",
                CharsetUtil.UTF_8,
                securityProperties.getAli().getPublicKey(),
                "RSA2"
        );
    }

    /**
     * 密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
