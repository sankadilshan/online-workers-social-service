package com.onlineWorkers.onlineWorkersSocialService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@SpringBootApplication
@EntityScan(basePackages = "com.onlineworkers.models")
@ComponentScan(basePackages = {"com.onlineWorkers.onlineWorkersSocialService.controller","com.onlineWorkers.onlineWorkersSocialService.service","com.onlineWorkers.onlineWorkersSocialService.config"})
public class OnlieWorkersSocialServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlieWorkersSocialServiceApplication.class, args);
	}



	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
