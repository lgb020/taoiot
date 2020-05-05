package com.github.taoroot.taoiot.netty.tcp.core;

import java.lang.annotation.*;

/**
 * 协议包类型
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PackageType {

    // 类型
    int value() default -1;

    // 描述
    String desc() default "";
}
