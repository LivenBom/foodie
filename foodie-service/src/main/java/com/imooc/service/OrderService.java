package com.imooc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.OrderQueryBO;

public interface OrderService {
    /**
     * 查询订单列表
     */
    Page<Orders> queryOrders(OrderQueryBO queryBO);
}
