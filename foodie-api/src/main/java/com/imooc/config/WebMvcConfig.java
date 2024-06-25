package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/") // 映射swagger2(之前是不要需要配置的会自动映射，但是现在加了映射方法之后需要手动添加)
                .addResourceLocations("file:/Users/liven/workspace/images/"); // 映射本地静态资源路径, 映射所有资源文件都可以通过Web服务访问
        // 比如这里本地完整的路径是这样的：file:/Users/liven/workspace/images/foodie/face/1901021A4Y1P6Z1F/face.png
        // 这里的写的映射地址是到：/Users/liven/workspace/images/
        // 那么部署之后，访问的地址就是：http://ip:port/foodie/face/1901021A4Y1P6Z1F/face.png
    }
}
