package com.imooc.controller.center;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.service.center.CenterUsersService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tags({@Tag(name = "用户中心我的订单")})
@RestController
@RequestMapping("myorders")
public class MyOrdersController {

    @Autowired
    private MyOrdersService myOrdersService;

    @PostMapping("/query")
    public IMOOCJSONResult query(@RequestParam String userId,
                                 @RequestParam(required = false) OrderStatusEnum orderStatus,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "20") Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        IPage<MyOrdersVO> orders = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(orders);
    }

}
