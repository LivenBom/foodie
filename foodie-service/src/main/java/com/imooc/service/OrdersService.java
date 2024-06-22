package com.imooc.service;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

/**
* @author liven
* @description 针对表【orders(订单表;)】的数据库操作Service
* @createDate 2024-06-01 22:56:20
*/
public interface OrdersService extends IService<Orders> {

    /*
    * 创建订单
    * */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);


    /*
    * 更新订单状态
    * */
    public void updateOrderStatus(String orderId, OrderStatusEnum orderStatus);


    /*
    * 查询订单状态
    * */
    public OrderStatus queryOrderStatusInfo(String orderId);

}
