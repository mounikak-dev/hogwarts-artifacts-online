package com.learn.hogwartsartifactsonline.security;

import com.learn.hogwartsartifactsonline.client.ai.chat.rediscache.RedisCacheClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final RedisCacheClient redisCacheClient;

    public JwtInterceptor(RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         //get the token from the request header
        String authorizationHeader = request.getHeader("Authorization");

         // if token is not null and starts with Bearer, then verify if its present in the redis
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String userId = jwt.getClaim("userId").toString();

            if(!this.redisCacheClient.isUserTokenInWhiteList(userId, jwt.getTokenValue())){
                throw new BadCredentialsException("Invalid token");
            }
        }

        return true;
    }
}
