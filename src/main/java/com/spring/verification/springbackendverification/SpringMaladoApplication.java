package com.spring.verification.springbackendverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class SpringMaladoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMaladoApplication.class, args);
	}

}
