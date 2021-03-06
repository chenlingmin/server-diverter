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
public class Server_A_Prod {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "prod");
        System.setProperty("server.port", "8081");
        SpringApplication.run(Server_A_Prod.class);
    }
}
