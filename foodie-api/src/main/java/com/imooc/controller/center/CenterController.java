package com.imooc.controller.center;

import com.imooc.pojo.Users;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tags({@Tag(name = "用户中心")})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUsersService centerUsersService;

    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(@RequestParam String userId) {
        if(StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        Users user = centerUsersService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(user);
    }

}
