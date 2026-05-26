package com.acneshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.acneshop.mapper")
public class AcneShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcneShopApplication.class, args);
    }
}
