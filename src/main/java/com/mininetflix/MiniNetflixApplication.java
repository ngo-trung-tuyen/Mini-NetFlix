package com.mininetflix;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class MiniNetflixApplication {
    public static void main(String[] args) {
        log.info("Starting MiniNetflixApplication");
        SpringApplication.run(MiniNetflixApplication.class, args);
    }

}
