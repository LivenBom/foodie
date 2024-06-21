package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrdersService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tags({@Tag(name = "订单相关")})
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {
        System.out.println(submitOrderBO.toString());
        if (submitOrderBO.getPayMethod() == PayMethod.WEIXIN.type &&
            submitOrderBO.getPayMethod() == PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持！");
        }

        // 1. 创建订单
        // 2. 创建订单后，移除购物车中已结算（已提交）的商品
        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        ordersService.createOrder(submitOrderBO);

        return IMOOCJSONResult.ok();
    }
}
