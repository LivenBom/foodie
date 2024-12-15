package com.imooc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {
    private static final String TOKEN_PREFIX = "token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final long ACCESS_TOKEN_DURATION = 2; // 2小时
    private static final long REFRESH_TOKEN_DURATION = 7; // 7天

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setUserToken(String userId, String token) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, ACCESS_TOKEN_DURATION, TimeUnit.HOURS);
    }

    public void setUserRefreshToken(String userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_DURATION, TimeUnit.DAYS);
    }

    public String getUserToken(String userId) {
        String key = TOKEN_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public String getUserRefreshToken(String userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteUserToken(String userId) {
        String tokenKey = TOKEN_PREFIX + userId;
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.delete(tokenKey);
        redisTemplate.delete(refreshTokenKey);
    }
}
