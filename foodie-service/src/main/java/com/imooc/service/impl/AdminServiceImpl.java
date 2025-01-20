package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imooc.mapper.AdminUserMapper;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.service.AdminService;
import com.imooc.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String ADMIN_TOKEN_PREFIX = "admin:token:";
    private static final long TOKEN_EXPIRE_HOURS = 24;

    @Override
    public AdminUser login(AdminLoginBO loginBO) {
        log.info("AdminService - 尝试登录用户: {}", loginBO.getUsername());

        // 1. 查询用户
        LambdaQueryWrapper<AdminUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUser::getUsername, loginBO.getUsername())
                   .eq(AdminUser::getStatus, 1);  // 只查询启用状态的用户
        
        AdminUser adminUser = adminUserMapper.selectOne(queryWrapper);
        if (adminUser == null) {
            log.warn("AdminService - 用户不存在: {}", loginBO.getUsername());
            return null;
        }

        // 2. 验证密码
        try {
            String encryptedPassword = MD5Utils.getMD5StrAdmin(loginBO.getPassword());
            if (!encryptedPassword.equals(adminUser.getPassword())) {
                log.warn("AdminService - 密码错误: {}", loginBO.getUsername());
                return null;
            }
        } catch (Exception e) {
            log.error("AdminService - 密码加密失败", e);
            return null;
        }

        // 3. 生成新的 token
        String token = adminUser.getId().toString();
        String redisKey = ADMIN_TOKEN_PREFIX + token;
        log.info("AdminService - 生成token: {}, Redis key: {}", token, redisKey);

        redisTemplate.opsForValue().set(
            redisKey,
            adminUser.getId().toString(),
            TOKEN_EXPIRE_HOURS,
            TimeUnit.HOURS
        );

        // 4. 更新最后登录时间
        adminUser.setLastLoginTime(new Date());
        adminUserMapper.updateById(adminUser);

        // 5. 清除敏感信息
        adminUser.setPassword(null);
        adminUser.setToken(token);
        
        log.info("AdminService - 登录成功: {}, token: {}", adminUser.getUsername(), token);
        return adminUser;
    }

    @Override
    public AdminUser getAdminByToken(String token) {
        log.info("AdminService - 验证token: {}", token);
        
        // 1. 从Redis获取用户ID
        String redisKey = ADMIN_TOKEN_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(redisKey);
        
        if (userId == null) {
            log.warn("AdminService - Token不存在或已过期: {}", token);
            return null;
        }

        log.info("AdminService - Redis中找到userId: {}", userId);

        // 2. 刷新token有效期
        redisTemplate.expire(redisKey, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

        // 3. 查询用户信息
        AdminUser adminUser = adminUserMapper.selectById(userId);
        if (adminUser != null) {
            adminUser.setPassword(null);  // 清除敏感信息
            log.info("AdminService - 用户验证成功: {}", adminUser.getUsername());
        } else {
            log.warn("AdminService - 未找到用户: {}", userId);
        }
        
        return adminUser;
    }

    @Override
    public void logout(String token) {
        log.info("AdminService - 用户登出，删除token: {}", token);
        redisTemplate.delete(ADMIN_TOKEN_PREFIX + token);
    }
}
