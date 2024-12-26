package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UsersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.JwtUtils;
import com.imooc.utils.RedisOperator;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tags({@Tag(name = "登录注册相关的接口")})
@RestController
@RequestMapping("/passport")
public class PassportController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisOperator redisOperator;

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

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
            StringUtils.isBlank(password)) {
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

        // 4. 实现注册
        Users userResult = usersService.createUser(userBO);

        // 5. 移除用户敏感信息
        userResult = setNullProperty(userResult);

        // 6. 设置cookie
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

        // 2. 实现登录
        Users userResult = usersService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        // 3. 生成token和refreshToken并保存到redis
        String userId = userResult.getId();
        String accessToken = jwtUtils.generateAccessToken(userId);
        String refreshToken = jwtUtils.generateRefreshToken(userId);
        redisOperator.setUserToken(userId, accessToken);
        redisOperator.setUserRefreshToken(userId, refreshToken);

        // 4. 移除用户敏感信息
        userResult = setNullProperty(userResult);

        // 5. 设置cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);
        
        // 6. 返回token和用户信息给前端
        Map<String, Object> map = new HashMap<>();
        map.put("token", accessToken);
        map.put("refreshToken", refreshToken);
        map.put("user", userResult);
        return IMOOCJSONResult.ok(map);
    }

    @PostMapping("/refresh")
    public IMOOCJSONResult refreshToken(@RequestParam String refreshToken) {
        // 1. 验证refresh token是否有效
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            return IMOOCJSONResult.errorMsg("Invalid refresh token");
        }

        // 2. 从refresh token中获取用户ID
        String userId = jwtUtils.getUserId(refreshToken);
        if (userId == null) {
            return IMOOCJSONResult.errorMsg("Invalid refresh token");
        }

        // 3. 验证refresh token是否与redis中的匹配
        String storedRefreshToken = redisOperator.getUserRefreshToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return IMOOCJSONResult.errorMsg("Refresh token has been revoked");
        }

        // 4. 生成新的access token和refresh token
        String newAccessToken = jwtUtils.generateAccessToken(userId);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId);

        // 5. 更新redis中的token
        redisOperator.setUserToken(userId, newAccessToken);
        redisOperator.setUserRefreshToken(userId, newRefreshToken);

        // 6. 返回新的token
        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return IMOOCJSONResult.ok(tokens);
    }

    @PostMapping("/logout")
    public IMOOCJSONResult logout(String userId) {
        // 清除用户token
        redisOperator.deleteUserToken(userId);
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
