package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.OrderItems;
import com.imooc.service.OrderItemsService;
import com.imooc.mapper.OrderItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【order_items(订单商品关联表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class OrderItemsServiceImpl extends ServiceImpl<OrderItemsMapper, OrderItems>
    implements OrderItemsService{

}




