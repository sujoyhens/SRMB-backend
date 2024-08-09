package com.boenci.srmb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SrmbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrmbApplication.class, args);
	}

}
