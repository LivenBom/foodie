package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.enums.OrderStatus;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.OrderQueryBO;
import com.imooc.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public Page<Orders> queryOrders(OrderQueryBO queryBO) {
        Page<Orders> page = new Page<>(queryBO.getPage(), queryBO.getPageSize());

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        
        // 按订单号查询
        if (StringUtils.isNotBlank(queryBO.getOrderId())) {
            queryWrapper.eq(Orders::getId, queryBO.getOrderId());
        }
        
        // 按用户ID查询
        if (StringUtils.isNotBlank(queryBO.getUserId())) {
            queryWrapper.eq(Orders::getUserId, queryBO.getUserId());
        }
        
        // 按状态查询
        if (queryBO.getStatus() != null) {
            for (OrderStatus status : OrderStatus.values()) {
                if (status.getType().equals(queryBO.getStatus())) {
                    queryWrapper.eq(Orders::getStatus, status);
                    break;
                }
            }
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(Orders::getCreatedTime);

        return ordersMapper.selectPage(page, queryWrapper);
    }
}
