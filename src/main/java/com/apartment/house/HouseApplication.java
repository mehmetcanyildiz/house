package com.apartment.house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class HouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseApplication.class, args);
    }

}
