package com.github.taoroot.taoiot.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author zhiyi
 */
public class JwtUtil {

    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_PARAMETER = "token";

    public static Claims parse(String jsonWebToken, String secret) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret != null ? secret : "")
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String create(String username, String secret, long expiration) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expiration;

        return Jwts.builder()
                .setNotBefore(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, secret != null ? secret : "")
                .compact();
    }
}