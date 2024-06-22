package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrdersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tags({@Tag(name = "订单相关")})
@RestController
@RequestMapping("/orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RestTemplate  restTemplate;

    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        System.out.println(submitOrderBO.toString());
        if (submitOrderBO.getPayMethod() == PayMethod.WEIXIN.type &&
            submitOrderBO.getPayMethod() == PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持！");
        }

        // 1. 创建订单
        OrderVO orderVO = ordersService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单后，移除购物车中已结算（已提交）的商品
        // TODO: 整合Reids之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        // CookieUtils.setCookie(request, response, "shopcart", "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        // 就是调用支付中心的接口（比如微信支付，支付宝支付接口）来生成支付订单
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        // 设置回调地址
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        // 构建请求对象
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        // 发起请求
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(paymentUrl, entity, Integer.class);
        // 获取请求结果
        Integer status = responseEntity.getBody();
        if (status != 200) {
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }
        return IMOOCJSONResult.ok(orderVO);
    }

    /*
    * 这个用于，微信支付成功后 -> 支付中心 -> 我们自己的平台回调
    * 只有支付成功的时候才会有，失败提示是微信SDK自己处理的
    * */
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER);
        return HttpStatus.OK.value();
    }

}
