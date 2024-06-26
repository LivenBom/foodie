package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemsService;
import com.imooc.service.OrderItemsService;
import com.imooc.service.OrdersService;
import com.imooc.mapper.OrdersMapper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author liven
* @description 针对表【orders(订单表;)】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer postAmount = 0; // 包邮：邮费设置为0

        String orderId = sid.nextShort();

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        // 1. 新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(userAddress.getProvince() + " "
                                    + userAddress.getCity() + " "
                                    + userAddress.getDistrict() + " "
                                    + userAddress.getDetail());
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO);
        newOrder.setIsDelete(YesOrNo.NO);

        // 2. 循环itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;        // 商品原价累计
        Integer realPayAmount = 0;      // 优惠后实际支付价格累计
        for (String itemSpecId : itemSpecIdArr) {
            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;

            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec spec = itemsService.queryItemSpecById(itemSpecId);
            totalAmount += spec.getPriceNormal() * buyCounts;
            realPayAmount += spec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = spec.getItemId();
            Items item = itemsService.queryItemById(itemId);
            String imgUrl = itemsService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(spec.getName());
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setPrice(spec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单后，规格表中需要扣除库存
            itemsService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        baseMapper.insert(newOrder);

        // 3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(newOrder.getId());
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY);
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. 构建商户订单，用于传给支付中心
        // 构建VO要传给支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        // 5. 构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, OrderStatusEnum orderStatus) {
        OrderStatus paidOrderStatus = new OrderStatus();
        paidOrderStatus.setOrderId(orderId);
        paidOrderStatus.setOrderStatus(orderStatus);
        paidOrderStatus.setPayTime(new Date());

        orderStatusMapper.updateById(paidOrderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectById(orderId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void closeOrder() {
        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭
        LambdaQueryWrapper<OrderStatus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderStatus::getOrderStatus, OrderStatusEnum.WAIT_PAY);
        queryWrapper.ge(OrderStatus::getCreatedTime, new Date(System.currentTimeMillis() - 24 * 3600 * 1000));
        orderStatusMapper.selectList(queryWrapper).forEach(orderStatus -> {
            orderStatus.setOrderStatus(OrderStatusEnum.CLOSE);
            orderStatus.setCloseTime(new Date());
            orderStatusMapper.updateById(orderStatus);
        });
    }
}




