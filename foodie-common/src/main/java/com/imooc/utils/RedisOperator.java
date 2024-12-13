package com.imooc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String TOKEN_PREFIX = "user:token:";
    private static final long TOKEN_EXPIRE = 7 * 24 * 60 * 60; // 7天
    
    public void setUserToken(String userId, String token) {
        String key = TOKEN_PREFIX + userId;
        // 先删除旧token（实现单点登录）
        redisTemplate.delete(key);
        // 设置新token
        redisTemplate.opsForValue().set(key, token, TOKEN_EXPIRE, TimeUnit.SECONDS);
    }
    
    public String getUserToken(String userId) {
        String key = TOKEN_PREFIX + userId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }
    
    public void deleteUserToken(String userId) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
