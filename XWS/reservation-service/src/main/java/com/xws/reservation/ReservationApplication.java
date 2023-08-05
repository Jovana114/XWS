package com.xws.reservation;

import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoRepositories
public class ReservationApplication implements CommandLineRunner {
    public static void main(String[] args) {

        SpringApplication.run(ReservationApplication.class, args);

    }

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {

        Server server= ServerBuilder
                .forPort(7575)
                .addService(new ReservationService(reservationRepository)).build();

        server.start();
        server.awaitTermination();
    }

}
