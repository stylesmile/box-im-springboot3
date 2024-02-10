package com.bx.api;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.bx.api.mapper"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})// 禁用secrity
public class ApiApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiApp.class, args);
    }
}
