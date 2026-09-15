package com.learn.hogwartsartifactsonline.security;

import com.learn.hogwartsartifactsonline.client.ai.chat.rediscache.RedisCacheClient;
import com.learn.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.learn.hogwartsartifactsonline.hogwartsuser.MyUserPrincipal;
import com.learn.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.learn.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final RedisCacheClient redisCacheClient;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter, RedisCacheClient redisCacheClient) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.redisCacheClient = redisCacheClient;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();
        UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);
        //create a JWT
        String token = this.jwtProvider.createToken(authentication);

        // save the token in redis, key is "whitelist:{userId}" value is token. expire time in 2 hours.
        this.redisCacheClient.set("whitelist:"+hogwartsUser.getId(), token, 2, TimeUnit.HOURS);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
