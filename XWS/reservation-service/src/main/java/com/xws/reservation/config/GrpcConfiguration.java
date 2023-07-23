package com.xws.reservation.config;

import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcConfiguration {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccommodationServiceGrpc.AccommodationServiceBlockingStub accommodationServiceBlockingStub;

    @Bean
    public Server grpcServer() throws IOException {
        Server server = ServerBuilder.forPort(7575)
                .addService(new ReservationService(reservationRepository))
                .build();
        server.start();
        return server;
    }

    @Bean
    public AccommodationServiceGrpc.AccommodationServiceBlockingStub accommodationServiceBlockingStub() {
      Channel accommodationChannel = ManagedChannelBuilder.forAddress("localhost", 8585).usePlaintext().build();
      return AccommodationServiceGrpc.newBlockingStub(accommodationChannel);
    }
}
