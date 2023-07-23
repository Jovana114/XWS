package com.example.Accommodationservice.config;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.service.AccommodationService;
import com.xws.accommodation.AccommodationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Config {

    private static final int GRPC_SERVER_PORT = 8585;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Bean
    public Server grpcServer() throws IOException {
        Server server = ServerBuilder.forPort(GRPC_SERVER_PORT)
                .addService(new AccommodationService(accommodationRepository))
                .build();
        server.start();
        return server;
    }
}