package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Orders;
import com.imooc.service.OrdersService;
import com.imooc.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【orders(订单表;)】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




