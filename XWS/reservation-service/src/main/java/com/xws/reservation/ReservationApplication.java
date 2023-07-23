package com.xws.reservation;

import com.xws.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EntityScan("com.xws.reservation.entity")
@ComponentScan(basePackages = {"com.xws.reservation.service", "com.xws.reservation.repository", "com.xws.reservation.config"})
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableMongoRepositories("com.xws.reservation.repository")
public class ReservationApplication {

    @Autowired
    private ReservationService reservationService;

    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }

}
