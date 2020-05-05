package com.github.taoroot.taoiot.netty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * @author : zhiyi
 * Date: 2020/2/17
 */
@Configuration
@EnableConfigurationProperties(NettyProperties.class)
@Import(NettyServer.class)
public class NettyAutoConfiguration {

    @Resource
    private NettyProperties nettyProperties;
}
