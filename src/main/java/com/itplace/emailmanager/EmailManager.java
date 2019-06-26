package com.itplace.emailmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailManager {

    public static void main(String[] args) {
        SpringApplication.run(EmailManager.class, args);
    }
}
