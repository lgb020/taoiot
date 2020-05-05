package com.github.taoroot.taoiot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author : zhiyi
 * Date: 2020/3/21
 */
public interface SecurityUserDetailsService extends UserDetailsService {


    UserDetails loadUserByWechat(String code) throws UsernameNotFoundException;

    UserDetails loadUserByAlipay(String code) throws UsernameNotFoundException;
}
