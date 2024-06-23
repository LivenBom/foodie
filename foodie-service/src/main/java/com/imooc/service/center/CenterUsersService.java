package com.imooc.service.center;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.bo.center.CenterUserBO;

/**
* @author liven
* @description 针对表【users(用户表 )】的数据库操作Service
* @createDate 2024-06-01 22:56:20
*/
public interface CenterUsersService extends IService<Users> {

    /*
    * 根据用户id查询用户信息
    * */
    public Users queryUserInfo(String userId);


    /*
    * 根据用户id，更新用户信息
    * */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
}
