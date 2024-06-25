package com.imooc.service.impl.center;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.UsersService;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.DateUtils;
import com.imooc.utils.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* @author liven
* @description 针对表【users(用户表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class CenterUsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements CenterUsersService {

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users user = baseMapper.selectById(userId);
        user.setPassword(null);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updatedUser = new Users();
        updatedUser.setId(userId);
        BeanUtils.copyProperties(centerUserBO, updatedUser);
        baseMapper.updateById(updatedUser);

        return queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updatedUser = new Users();
        updatedUser.setId(userId);
        updatedUser.setFace(faceUrl);
        baseMapper.updateById(updatedUser);
        return baseMapper.selectById(userId);
    }
}




