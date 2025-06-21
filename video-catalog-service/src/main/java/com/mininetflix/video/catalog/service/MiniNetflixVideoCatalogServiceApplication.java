package com.mininetflix.video.catalog.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.mininetflix.backend.common",
    "com.mininetflix.video.catalog.service"
})
public class MiniNetflixVideoCatalogServiceApplication {
    public static void main(String[] args) {
       SpringApplication.run(MiniNetflixVideoCatalogServiceApplication.class, args);
    }
}
