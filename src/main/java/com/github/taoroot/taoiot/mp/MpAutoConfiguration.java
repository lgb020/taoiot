package com.github.taoroot.taoiot.mp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author : zhiyi
 * Date: 2020/2/17
 */
@Configuration
@Import(MpEndpoint.class)
public class MpAutoConfiguration {
}
