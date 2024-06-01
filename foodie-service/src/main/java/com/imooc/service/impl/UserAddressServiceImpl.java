package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.UserAddress;
import com.imooc.service.UserAddressService;
import com.imooc.mapper.UserAddressMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【user_address(用户地址表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

}




