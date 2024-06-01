package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liven
* @description 针对表【order_status(订单状态表;订单的每个状态更改都需要进行记录
10：待付款  20：已付款，待发货  30：已发货，待收货（7天自动确认）  40：交易成功（此时可以评价）50：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
退货/退货，此分支流程不做，所以不加入)】的数据库操作Mapper
* @createDate 2024-06-01 22:56:20
* @Entity com.imooc.pojo.OrderStatus
*/
public interface OrderStatusMapper extends BaseMapper<OrderStatus> {

}




