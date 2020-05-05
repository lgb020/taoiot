package com.github.taoroot.taoiot.swagger;

import com.github.taoroot.taoiot.common.Const;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : zhiyi
 * Date: 2020/2/17
 */
@Data
@ConfigurationProperties(prefix = Const.PREFIX + ".swagger")
public class SwaggerProperties {

    /**
     * 名称
     */
    private String name = "swagger";

    /**
     * 版本号
     */
    private String version = "0.1";

    /**
     * 前缀
     */
    private String prefix = "/";

}
