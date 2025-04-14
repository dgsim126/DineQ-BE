package com.dineq.dineqbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DineqbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DineqbeApplication.class, args);
	}

}
