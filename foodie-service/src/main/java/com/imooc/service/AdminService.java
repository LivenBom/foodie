package com.imooc.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;

public interface AdminService {
    /**
     * 管理员登录
     * @param loginBO 登录信息
     * @return 登录成功返回用户信息和token，失败返回null
     */
    AdminUser login(AdminLoginBO loginBO);
    
    /**
     * 根据token获取管理员信息
     * @param token 令牌
     * @return 管理员信息
     */
    AdminUser getAdminByToken(String token);

    void logout(String token);
}
