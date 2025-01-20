package com.imooc.controller;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.service.AdminService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "管理员接口")
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody AdminLoginBO loginBO) {
        AdminUser adminUser = adminService.login(loginBO);
        if (adminUser == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码错误");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("user", adminUser);
        data.put("token", adminUser.getId().toString());  
        
        return IMOOCJSONResult.ok(data);
    }

    @GetMapping("/info")
    public IMOOCJSONResult getAdminInfo(@RequestHeader("Admin-Token") String token) {
        AdminUser adminUser = adminService.getAdminByToken(token);
        if (adminUser == null) {
            return IMOOCJSONResult.errorTokenMsg("token已失效，请重新登录");
        }
        return IMOOCJSONResult.ok(adminUser);
    }

    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestHeader("Admin-Token") String token) {
        adminService.logout(token);
        return IMOOCJSONResult.ok();
    }
}
