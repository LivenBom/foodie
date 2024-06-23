package com.imooc.controller.center;

import com.imooc.enums.Sex;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tags({@Tag(name = "用户信息接口")})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUsersService centerUsersService;

    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestParam String userId,
                                  @RequestBody CenterUserBO centerUserBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Users user = centerUsersService.updateUserInfo(userId, centerUserBO);
        // 更新前端的cookie
        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return IMOOCJSONResult.ok(user);
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
