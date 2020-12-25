package com.chenlm.cloud.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class Server_A_Gray {
    public static void main(String[] args) {
//        System.setProperty("spring.profiles.active", "gray");
        System.setProperty("server.port", "8082");
        SpringApplication.run(Server_A_Gray.class);
    }
}
