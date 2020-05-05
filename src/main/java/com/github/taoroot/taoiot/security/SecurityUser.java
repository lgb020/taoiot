package com.github.taoroot.taoiot.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author : zhiyi
 * Date: 2020/2/13
 */
@Getter
@Setter
public class SecurityUser extends User {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 登录方式
     */
    private LoginType type;
    /**
     * 微信公众号openId
     */
    private String wxMpOpenid;
    /**
     * 支付宝公众号openId
     */
    private String aliMpOpenid;


    public SecurityUser(Integer id, LoginType loginType, String wxMpOpenid, String aliMpOpenid, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.type = loginType;
        this.wxMpOpenid = wxMpOpenid;
        this.aliMpOpenid = aliMpOpenid;
    }
}
