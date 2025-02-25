package com.netzwerk.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class EcommerceApplication extends SpringBootServletInitializer {
    public static void main(String[] args)  {
        SpringApplication.run(EcommerceApplication.class, args);

    }
}