package com.imooc.service;

import com.imooc.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.bo.UserBO;

/**
* @author liven
* @description 针对表【users(用户表 )】的数据库操作Service
* @createDate 2024-06-01 22:56:20
*/
public interface UsersService extends IService<Users> {

    /*
    * 判断用户名是否存在
    * */
    public boolean queryUsernameIsExist(String username);

    /*
    * 注册新用户
    * */
    public Users createUser(UserBO userBO);

    /*
    * 创建新用户
    * */
    public Users createUser(Users user);

    /*
    * 检索用户名和密码是否匹配，用于登录
    * */
    public Users queryUserForLogin(String username, String password);

    /**
     * 根据 Apple ID 查询用户
     */
    Users queryUserByAppleId(String appleId);
}
