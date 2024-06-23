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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tags({@Tag(name = "用户信息接口")})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUsersService centerUsersService;

    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestParam String userId,
                                  @RequestBody @Valid CenterUserBO centerUserBO,
                                  BindingResult result,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        // 目前加的验证都不起作用（尚未解决）
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            return IMOOCJSONResult.errorMap(getErrors(result));
        }

        Users user = centerUsersService.updateUserInfo(userId, centerUserBO);
        // 更新前端的cookie
        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return IMOOCJSONResult.ok(user);
    }


    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError fieldError : errorList) {
            String errorField = fieldError.getField();
            String errorMsg = fieldError.getDefaultMessage();
            map.put(errorField, errorMsg);
        }
        return map;
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
