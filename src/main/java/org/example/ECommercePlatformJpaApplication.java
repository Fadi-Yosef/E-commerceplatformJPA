package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example", "se.lexicon.ecommerceworkshop"})
public class ECommercePlatformJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommercePlatformJpaApplication.class, args);
    }
}
