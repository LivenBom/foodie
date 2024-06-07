package com.imooc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Swagger2 implements WebMvcConfigurer {

    @Value("${spring.application.name:我的应用}")
    private String applicationName;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("天天吃货 电商平台接口api")
                        .description("专为天天吃货提供的api文档")
                        .version("1.0.0")
                        .termsOfService("https://www.imooc.com")
                        .contact(new Contact()
                                .name("liven")
                                .email("123456@163.com"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("二次开发文档")
                        .url("https://www.imooc.com"));
    }

}
