package com.tbfp.teamplannerbe.domain.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    private final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 30; // 30 min
    private final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; // 14 week

    private final String HEADER_NAME = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(String loginId) {
        return JWT.create()
                .withSubject(loginId)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("loginId", loginId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
    public String generateRefreshToken(String loginId) {
        return JWT.create()
                .withSubject(loginId)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("loginId", loginId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public boolean verifyToken(String token) {
        log.info("SECRET_KEY = " + SECRET_KEY);
        try {
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getHeader(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (header != null && !header.startsWith(TOKEN_PREFIX)) header = null;
        return header;
    }

    public String getLoginId(HttpServletRequest request) {
        String accessToken = getHeader(request).replace(TOKEN_PREFIX, "");

        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(accessToken)
                .getClaim("loginId").asString();
    }
    public String getLoginIdFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token)
                .getClaim("loginId").asString();
    }
}
