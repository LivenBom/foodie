package com.imooc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.OrderQueryBO;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "订单接口")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "订单列表")
    @GetMapping("/list")
    public IMOOCJSONResult list(OrderQueryBO queryBO) {
        Page<Orders> page = orderService.queryOrders(queryBO);
        return IMOOCJSONResult.ok(page);
    }
}
