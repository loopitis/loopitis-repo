package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoopitisApplication {

    public static final String MY_LOGGER = "my.logger";
    private static final Logger log = LoggerFactory.getLogger(LoopitisApplication.MY_LOGGER);

    public static void main(String[] args) {
        log.debug("Loopitis Srarts !");
        SpringApplication.run(LoopitisApplication.class, args);

    }


}

