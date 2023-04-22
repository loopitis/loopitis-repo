package com.example.demo;

import consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static final String MY_LOGGER = "my.logger";
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.MY_LOGGER);

	public static void main(String[] args) {
		log.debug("HEEEEEEEEEEEEEEEEEEEEEY");
		SpringApplication.run(DemoApplication.class, args);

	}



}

