package com.github.taoroot.taoiot.security.annotation;

import java.lang.annotation.*;

/**
 * 免鉴权
 *
 * @author zhiyi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotAuth {

}
