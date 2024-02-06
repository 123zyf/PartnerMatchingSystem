package com.example.partnermatchingsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.partnermatchingsystem.mapper")
@SpringBootApplication
public class PartnerMatchingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerMatchingSystemApplication.class, args);
    }

}
