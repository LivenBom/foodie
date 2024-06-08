package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UsersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tags({@Tag(name = "登录注册相关的接口")})
@RestController
@RequestMapping("/passport")
public class PassportController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        // 1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        // 2. 查询注册的用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        // 3. 请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
            StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 查询用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        // 3. 密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }

        // 4. 判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }

        // 5. 实现注册
        Users userResult = usersService.createUser(userBO);

        // 6. 移除用户敏感信息
        userResult = setNullProperty(userResult);

        // 7. 设置cookie
        // 这样网页前端就能通过cookie获取到登录的用户信息，保存登录的状态
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        return IMOOCJSONResult.ok();
    }

    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 判断是否有这个用户
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (!isExist) {
            return IMOOCJSONResult.errorMsg("用户名不存在");
        }

        // 2. 实现登录
        Users userResult = usersService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        // 3. 移除用户敏感信息
        userResult = setNullProperty(userResult);

        // 4. 设置cookie
        // 这样网页前端就能通过cookie获取到登录的用户信息，保存登录的状态
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        return IMOOCJSONResult.ok(userResult);
    }

    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据
        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
