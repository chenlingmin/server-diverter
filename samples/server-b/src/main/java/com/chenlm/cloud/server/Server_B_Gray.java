package com.chenlm.cloud.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Server_B_Gray {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "gray");
        System.setProperty("server.port", "8085");
        SpringApplication.run(Server_B_Gray.class);
    }
}
