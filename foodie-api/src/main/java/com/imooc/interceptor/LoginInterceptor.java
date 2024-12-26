package com.imooc.interceptor;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.JwtUtils;
import com.imooc.utils.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        logger.info("拦截请求: " + path);

        // 检查是否为排除的路径
        if (path.startsWith("/post/categories") || 
            path.startsWith("/post/topics") || 
            path.startsWith("/post/write/create")) {
            logger.info("允许访问排除的路径: " + path);
            return true;
        }

        // 1. 获取请求头中的token
        String token = request.getHeader("Authorization");
        logger.info("Token: " + (token != null ? token.substring(0, Math.min(token.length(), 10)) + "..." : "null"));
        
        if (StringUtils.isBlank(token)) {
            logger.info("未找到token，路径: " + path);
            returnError(response, "请登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 2. 验证token是否有效
        if (!jwtUtils.validateToken(token)) {
            logger.info("无效的token，路径: " + path);
            returnError(response, "登录已过期，请重新登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3. 从token中获取用户ID
        String userId = jwtUtils.getUserId(token);
        logger.info("用户 " + userId + " 访问路径: " + path);
        
        // 4. 检查redis中是否存在该token（实现单点登录）
        String cachedToken = redisOperator.getUserToken(userId);
        if (StringUtils.isBlank(cachedToken) || !token.equals(cachedToken)) {
            logger.info("Redis中的token不匹配，用户: " + userId);
            returnError(response, "您的账号已在其他设备登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private void returnError(HttpServletResponse response, String msg, int statusCode) throws Exception {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtils.objectToJson(IMOOCJSONResult.errorMsg(msg)));
    }
}
