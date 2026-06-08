package com.artshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.artshop.repository")
public class ArtShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtShopApplication.class, args);
    }

}
