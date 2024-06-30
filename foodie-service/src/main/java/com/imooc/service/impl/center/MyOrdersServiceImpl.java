package com.imooc.service.impl.center;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.mapper.OrdersMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.center.CenterUsersService;
import com.imooc.service.center.MyOrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
* @author liven
* @description 针对表【orders(订单表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class MyOrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements MyOrdersService {

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IPage<MyOrdersVO> queryMyOrders(String userId, OrderStatusEnum orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus.type);
        }

        Page<MyOrdersVO> pageItem = new Page<>(page, pageSize);
        return baseMapper.queryMyOrders(map, pageItem);
    }
}




