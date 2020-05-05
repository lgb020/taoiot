package com.github.taoroot.taoiot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taoroot.taoiot.common.R;
import lombok.experimental.UtilityClass;

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


    /**
     * 获取当前的用户
     */
    public Integer getUserId() {
        return -1;
    }


    public void writeToResponse(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(r));
        printWriter.flush();
    }
}
