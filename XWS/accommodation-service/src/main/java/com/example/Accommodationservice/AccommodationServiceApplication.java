package com.example.Accommodationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccommodationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccommodationServiceApplication.class, args);
	}

}
