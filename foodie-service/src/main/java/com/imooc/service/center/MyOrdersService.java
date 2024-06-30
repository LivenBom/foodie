package com.imooc.service.center;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.MyOrdersVO;

/**
* @author liven
* @description 针对表【Orders(订单表 )】的数据库操作Service
* @createDate 2024-06-01 22:56:20
*/
public interface MyOrdersService extends IService<Orders> {

    /*
    * 查询我的订单列表
    * */
    public IPage<MyOrdersVO> queryMyOrders(String userId, OrderStatusEnum orderStatus, Integer page, Integer pageSize);
}
