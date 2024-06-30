package com.imooc.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author liven
* @description 针对表【orders(订单表;)】的数据库操作Mapper
* @createDate 2024-06-01 22:56:20
* @Entity com.imooc.pojo.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {
    public Page<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map,
                                          Page<MyOrdersVO> pageItem);
}




