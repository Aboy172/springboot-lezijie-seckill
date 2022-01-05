package com.example.java2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.example.java2.mapper")
public class Java2Application {

    public static void main(String[] args) {
        SpringApplication.run(Java2Application.class, args);
    }

}
