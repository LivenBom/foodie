package com.imooc.interceptor;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.JwtUtils;
import com.imooc.utils.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            returnError(response, "请登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 2. 验证token是否有效
        if (!jwtUtils.validateToken(token)) {
            returnError(response, "登录已过期，请重新登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3. 从token中获取用户ID
        String userId = jwtUtils.getClaimsFromToken(token).get("userId", String.class);
        
        // 4. 检查redis中是否存在该token（实现单点登录）
        String cachedToken = redisOperator.getUserToken(userId);
        if (StringUtils.isBlank(cachedToken) || !token.equals(cachedToken)) {
            returnError(response, "您的账号已在其他设备登录", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private void returnError(HttpServletResponse response, String msg, int statusCode) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(JsonUtils.objectToJson(IMOOCJSONResult.errorMsg(msg)));
    }
}
