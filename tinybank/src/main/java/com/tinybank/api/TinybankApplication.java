package com.tinybank.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TinybankApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinybankApplication.class, args);
    }

}