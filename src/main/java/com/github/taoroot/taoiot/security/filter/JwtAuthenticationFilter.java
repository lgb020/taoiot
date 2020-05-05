package com.github.taoroot.taoiot.security.filter;

import com.github.taoroot.taoiot.security.SecurityProperties;
import com.github.taoroot.taoiot.security.SecurityUser;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhiyi
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final SecurityProperties securityProperties;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 请求头带AUTHORIZATION
        String authorization = request.getHeader(JwtUtil.AUTHORIZATION);

        if (!StringUtils.isEmpty(authorization)) {
            Claims claims = JwtUtil.parse(authorization, securityProperties.getToken().getSecret());
            auth(request, claims);
        }

        // 请求参数带token
        String token = request.getParameter(JwtUtil.AUTHORIZATION_PARAMETER);
        Claims claims = JwtUtil.parse(token, securityProperties.getToken().getSecret());
        if (!StringUtils.isEmpty(token)) {
            auth(request, claims);
        }

        filterChain.doFilter(request, response);
    }

    private void auth(HttpServletRequest request, Claims claims) {
        if (claims != null) {
            String username = claims.getSubject();
            if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }
}