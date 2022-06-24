package com.example.webmailcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class WebmailCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebmailCoreApplication.class, args);
    }

}
