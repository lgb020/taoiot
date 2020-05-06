package com.github.taoroot.taoiot.swagger;

import java.lang.annotation.*;

/**
 * 避免Swagger扫描
 *
 * @author zhiyi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotSwagger {

}
