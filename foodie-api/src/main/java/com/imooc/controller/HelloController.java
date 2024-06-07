package com.imooc.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* @Controller 和 @RestController的区别：
* 两者都是用于创建Web控制器的，但是前者返回的是视图，后者返回的是数据
* @Controller：
*   返回类型是一个视图名（如JSP、Thymeleaf模版等），SpringMVC将解析该视图名以生成HTML响应请求
*   如果要返回数据，需要使用@ResponseBody注解单独标记在方法上，这样Spring才会将方法返回的数据转换为JSON格式
*
* @RestController：
*   @RestController是一个组合注解，包含了@Controller和@ResponseBody注解
*   专为RESTful Web服务设计，适合服务API接口，返回的是数据（如JSON和XML），而不是视图
* */
@Hidden
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        return "Hello World";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        // session.removeAttribute("userInfo");
        return "ok";
    }
}
