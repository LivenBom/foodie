package com.imooc.service;

import com.imooc.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
