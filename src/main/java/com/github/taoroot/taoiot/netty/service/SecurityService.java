package com.github.taoroot.taoiot.netty.service;

/**
 * @author : zhiyi
 * Date: 2020/5/6
 */
public interface SecurityService {

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @return
     */
    boolean login(String username, String password);
}
