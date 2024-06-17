package com.imooc.controller;

import com.imooc.pojo.bo.ShopcarBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


@Tags({@Tag(name = "购物车")})
@RequestMapping("shopcart")
@RestController
public class ShopcarController {

    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcarBO shopcarBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        System.out.println(shopcarBO);

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步到redis缓存
        return IMOOCJSONResult.ok();
    }

}