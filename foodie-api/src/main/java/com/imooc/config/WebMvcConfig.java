package com.imooc.config;

import com.imooc.interceptor.LoginInterceptor;
import com.imooc.interceptor.AdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    
    @Autowired
    private AdminInterceptor adminInterceptor;

    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/") // 映射swagger2
                .addResourceLocations("classpath:/static/") // 映射静态资源
                .addResourceLocations("file:/Users/liven/workspace/images/"); // 映射本地静态资源
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 普通用户拦截器，排除管理后台相关路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")  // 只拦截API请求
                .excludePathPatterns(
                    "/admin/**",           // 排除管理后台的所有路径
                    "/passport/login",
                    "/passport/logout",
                    "/passport/regist",
                    "/passport/usernameIsExist",
                    "/error"
                );

        // 管理后台拦截器
        List<String> excludePatterns = new ArrayList<>();
        // 登录相关
        excludePatterns.add("/admin/login");
        excludePatterns.add("/admin/login.html");
        // 静态资源
        excludePatterns.add("/admin/static/**");
        excludePatterns.add("/admin/assets/**");
        excludePatterns.add("/admin/**/*.html");
        excludePatterns.add("/admin/**/*.js");
        excludePatterns.add("/admin/**/*.css");
        excludePatterns.add("/admin/**/*.png");
        excludePatterns.add("/admin/**/*.jpg");
        excludePatterns.add("/admin/**/*.jpeg");
        excludePatterns.add("/admin/**/*.gif");
        excludePatterns.add("/admin/**/*.svg");
        excludePatterns.add("/admin/**/*.ico");
        excludePatterns.add("/admin/**/*.woff");
        excludePatterns.add("/admin/**/*.woff2");
        excludePatterns.add("/admin/**/*.ttf");
        excludePatterns.add("/admin/**/*.eot");
        
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**", "/order/**")
                .excludePathPatterns(excludePatterns);
    }
}
