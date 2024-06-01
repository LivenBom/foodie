package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Users;
import com.imooc.service.UsersService;
import com.imooc.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【users(用户表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




