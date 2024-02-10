package com.bx.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "hello首页模块")
@RestController
public class HelloController {
    @Parameter(name = "name", description = "姓名", required = true)
    @Operation(summary = "hello接口向客人问好")
    @GetMapping("hello")
    public String hello(String name) {
        return "hello";
    }
}
