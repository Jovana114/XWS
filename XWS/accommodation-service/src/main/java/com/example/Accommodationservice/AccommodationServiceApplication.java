package com.example.Accommodationservice;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.example.Accommodationservice.service.AccommodationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
public class AccommodationServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AccommodationServiceApplication.class, args);
	}

	@Autowired
	AccommodationRepository accommodationRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Override
	public void run(String... args) throws Exception {

		Server server= ServerBuilder
				.forPort(8585)
				.addService(new AccommodationService(accommodationRepository, reservationRepository)).build();

		server.start();
		server.awaitTermination();
	}


}
