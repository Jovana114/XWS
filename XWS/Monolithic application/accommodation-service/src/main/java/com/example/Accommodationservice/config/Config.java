package com.example.Accommodationservice.config;

import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.service.AccommodationService;
import com.xws.accommodation.AccommodationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class Config {

//    @Autowired
//    private AccommodationRepository accommodationRepository;
//
//    @Bean
//    public ManagedChannel managedChannel() {
//        return ManagedChannelBuilder.forAddress("localhost", 8585)
//                .usePlaintext()
//                .build();
//    }
//
//    @Bean
//    @GrpcClient("accommodation-service")
//    public AccommodationServiceGrpc.AccommodationServiceBlockingStub accommodationServiceBlockingStub(ManagedChannel managedChannel) {
//        return AccommodationServiceGrpc.newBlockingStub(managedChannel);
//    }
//
//    @Bean
//    public AccommodationService accommodationService() {
//        return new AccommodationService(accommodationRepository);
//    }

}