package com.github.taoroot.taoiot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taoroot.taoiot.common.R;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 安全工具类
 *
 * @author zhiyi
 */
@UtilityClass
public class SecurityUtil {

    public static final String DEFAULT_ROLE = "ROLE_USER";

    public void writeToResponse(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(r));
        printWriter.flush();
    }


    /**
     * 获取Authentication
     */
    public UsernamePasswordAuthenticationToken getAuthentication() {
        return (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取用户
     */
    public SecurityUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (SecurityUser) principal;
        }
        return null;
    }

    /**
     * 获取当前的用户
     */
    public SecurityUser getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }

    /**
     * 获取当前的用户
     */
    public Integer getUserId() {
        SecurityUser user = getUser();
        if (user != null) {
            return user.getId();
        }
        return 0;
    }
}
