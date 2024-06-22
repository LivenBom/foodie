package com.imooc.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    // 支付中心的调用地址
    String paymentUrl = "";

    // 支付回调通知地址
    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
}
