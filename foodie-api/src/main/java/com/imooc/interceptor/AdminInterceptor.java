package com.imooc.interceptor;

import com.imooc.pojo.AdminUser;
import com.imooc.service.AdminService;
import com.imooc.utils.IMOOCJSONResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("AdminInterceptor - 拦截请求: {}", requestURI);

        // 暂时都不需要登录要求，全部放行
        return true;

//        // 如果是登录请求，直接放行
//        if (requestURI.contains("/admin/login")) {
//            log.info("AdminInterceptor - 登录请求，放行");
//            return true;
//        }
//
//        // 检查管理员Token
//        String token = request.getHeader("Admin-Token");
//        log.info("AdminInterceptor - Token: {}", token);
//
//        if (token == null || token.isEmpty()) {
//            log.warn("AdminInterceptor - Token为空");
//            // 如果是页面请求，重定向到登录页
//            if (requestURI.endsWith(".html")) {
//                log.info("AdminInterceptor - HTML请求，重定向到登录页");
//                response.sendRedirect("/admin/login.html");
//                return false;
//            }
//            // 如果是API请求，返回JSON错误信息
//            returnErrorResponse(response, IMOOCJSONResult.errorMsg("请先登录"));
//            return false;
//        }
//
//        // 验证token
//        AdminUser adminUser = adminService.getAdminByToken(token);
//        if (adminUser == null) {
//            log.warn("AdminInterceptor - Token无效: {}", token);
//            if (requestURI.endsWith(".html")) {
//                log.info("AdminInterceptor - HTML请求，重定向到登录页");
//                response.sendRedirect("/admin/login.html");
//                return false;
//            }
//            returnErrorResponse(response, IMOOCJSONResult.errorTokenMsg("token已失效，请重新登录"));
//            return false;
//        }
//
//        log.info("AdminInterceptor - Token有效，用户: {}", adminUser.getUsername());
//        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private void returnErrorResponse(HttpServletResponse response, IMOOCJSONResult result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.write(objectMapper.writeValueAsString(result));
        }
    }
}
