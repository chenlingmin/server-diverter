package com.chenlm.cloud.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    @Value("${spring.profiles.active}")
    private String value;
    @Value("${spring.application.name}")
    private String name;

    @GetMapping("test")
    public String get(HttpServletRequest request) {
        System.out.println(request.getHeader("x-env"));
        return name + "-" + value;
    }
}
