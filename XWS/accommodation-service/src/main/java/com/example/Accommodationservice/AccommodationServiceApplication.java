package com.example.Accommodationservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "com.example.Accommodationservice.repository")
@ComponentScan(basePackages = {"com.example.Accommodationservice.service", "com.example.Accommodationservice.repository"})
public class AccommodationServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccommodationServiceApplication.class, args);

	}


}
