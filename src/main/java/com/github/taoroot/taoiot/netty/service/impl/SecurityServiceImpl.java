package com.github.taoroot.taoiot.netty.service.impl;

import com.github.taoroot.taoiot.netty.SecurityService;
import org.springframework.stereotype.Component;

/**
 * @author : zhiyi
 * Date: 2020/5/6
 */
@Component
public class SecurityServiceImpl implements SecurityService {
    @Override
    public boolean login(String username, String password) {
        return true;
    }

}
