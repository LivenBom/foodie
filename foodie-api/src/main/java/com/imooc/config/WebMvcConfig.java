package com.imooc.config;

import com.imooc.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    // Removed redundant RestTemplate bean definition

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
        List<String> excludePaths = Arrays.asList(
                "/passport/login",
                "/passport/logout",
                "/passport/regist",
                "/passport/usernameIsExist",
                "/auth/apple/login",     // 添加Apple登录路径
                "/auth/apple/callback",  // 添加Apple回调路径
                "/error",               // 添加错误页面路径
                "/post/categories/**",
                "/post/topics/**",
                "/post/write/create/**",
                "/post/category/**",    // 添加分类管理接口
                "/app/**",              // 添加App相关接口
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.jpeg",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.ico",
                "/**/*.html"
        );

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePaths);
    }
}
