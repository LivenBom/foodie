package com.imooc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
* @SpringBootApplication
* 是一个组合注解，包含了@ComponentScan、@Configuration、@EnableAutoConfiguration
* 用于启动SpringBoot的自动配置，会自动扫描当前包和子包的所有bean，将其注册到Spring容器中
* @Configuration：
*   标记该类作为配置类，基于Java的Spring应用程序配置
* @EnableAutoConfiguration：
*   告诉SpringBoot基于类路径设置、其他bean以及各种属性设置开始添加beans
* @ComponentScan：
*   告诉Spring在哪里寻找其他的应用组件，通常是使用@Component，@Service，@Controller等注解的类
* */

// MyBatis: 扫描mapper接口所在的
@MapperScan("com.imooc.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
@EnableScheduling    // 开启定时任务
@SpringBootApplication
public class Application {
    // main是Java应用的入口
    public static void main(String[] args) {
        // SpringApplication.run(Application.class, args)启动SpringBoot应用
        SpringApplication.run(Application.class, args);
    }
}
