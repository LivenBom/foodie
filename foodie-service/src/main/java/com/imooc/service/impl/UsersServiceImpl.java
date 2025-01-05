package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.Sex;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UsersService;
import com.imooc.mapper.UsersMapper;
import com.imooc.utils.DateUtils;
import com.imooc.utils.MD5Utils;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* @author liven
* @description 针对表【users(用户表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{
    // 默认头像URL
    private static final String USER_FACE = "https://liven-blog.oss-cn-beijing.aliyuncs.com/2021/06/20210601225620.jpg";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername, username);
        Users result = baseMapper.selectOne(queryWrapper);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users user = new Users();
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        // 默认头像
        user.setFace(USER_FACE);
        // 默认生日
        user.setBirthday(DateUtils.stringToDate("1900-01-01"));
        // 默认性别为 保密
        user.setSex(Sex.secret);

        baseMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(Users user) {
        // 设置默认头像
        if (user.getFace() == null) {
            user.setFace(USER_FACE);
        }
        // 设置默认生日
        if (user.getBirthday() == null) {
            user.setBirthday(DateUtils.stringToDate("1900-01-01"));
        }
        // 设置默认性别为 保密
        if (user.getSex() == null) {
            user.setSex(Sex.secret);
        }

        baseMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Users::getUsername, username)
                .eq(Users::getPassword, password);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Users queryUserByAppleId(String appleId) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apple_id", appleId);
        return baseMapper.selectOne(queryWrapper);
    }
}
