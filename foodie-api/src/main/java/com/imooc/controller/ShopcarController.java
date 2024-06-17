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


    @PostMapping("/del")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的数据
        return IMOOCJSONResult.ok();
    }

}