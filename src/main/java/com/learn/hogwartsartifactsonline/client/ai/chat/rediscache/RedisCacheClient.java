package com.learn.hogwartsartifactsonline.client.ai.chat.rediscache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisCacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, String value, long timeOut, TimeUnit timeUnit) {
        this.stringRedisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    public String get(String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        this.stringRedisTemplate.delete(key);
    }

    public boolean isUserTokenInWhiteList(String userId, String tokenFromRequest) {
        String tokenFromRedis = this.get("whitelist:"+userId);
        return tokenFromRedis != null && tokenFromRedis.equals(tokenFromRequest);
    }
}
