package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.CarouselMapper;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.service.CarouselService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author liven
* @description 针对地址表的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class AddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements AddressService {

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer isDefault = 0;
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        if (addressList == null || addressList.isEmpty() || addressList.size() == 0) {
            isDefault = 1;
        }
        // 2. 保存到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);
        newAddress.setIsDefault(isDefault);
        baseMapper.insert(newAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);
        pendingAddress.setId(addressBO.getAddressId());
        baseMapper.updateById(pendingAddress);
    }

    @Override
    public void deleteUserAddress(String userId, String addressId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId);
        queryWrapper.eq(UserAddress::getId, addressId);
        baseMapper.delete(queryWrapper);
    }
}




