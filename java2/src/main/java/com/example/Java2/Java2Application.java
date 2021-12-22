package com.example.Java2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

@MapperScan(basePackages = "com.example.java2.mapper")
public class Java2Application {

    public static void main(String[] args) {
        SpringApplication.run(Java2Application.class, args);
    }

}
