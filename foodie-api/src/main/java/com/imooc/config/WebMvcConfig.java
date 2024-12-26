package com.imooc.config;

import com.imooc.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

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
                "/post/categories/**",
                "/post/topics/**",
                "/post/write/create/**",
                "/post/**",                  // 允许访问所有静态资源
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
