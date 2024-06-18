package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

/**
* @author liven
* @description 针对表【carousel(轮播图 )】的数据库操作Service
* @createDate 2024-06-01 22:56:19
*/
public interface AddressService extends IService<UserAddress> {

    /*
    * 根据用户id查询用户的收货地址列表
    * */
    public List<UserAddress> queryAll(String userId);

    /*
    * 用户新增地址
    * */
    public void addNewAddress(AddressBO addressBO);

    /*
    * 修改用户地址信息
    * */
    public void updateUserAddress(AddressBO addressBO);

    /*
    * 删除用户地址信息
    * */
    public void deleteUserAddress(String userId, String addressId);

    /*
    * 设置默认地址
    * */
    public void updateUserAddressToBeDefault(String userId, String addressId);
}
