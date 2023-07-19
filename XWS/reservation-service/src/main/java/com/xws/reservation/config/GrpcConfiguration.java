package com.xws.reservation.config;

import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

    //@Autowired
//    @GrpcClient("accommodation-service")
//    private AccommodationServiceGrpc.AccommodationServiceBlockingStub accommodationServiceBlockingStub;
//
//    @GrpcClient("reservation-service")
//    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationServiceBlockingStub;

    @Autowired
    private ReservationRepository reservationRepository;
//
//    @Bean
//    public ManagedChannel managedChannel() {
//        // Create and return the ManagedChannel bean
//        return ManagedChannelBuilder.forAddress("localhost", 7575)
//                .usePlaintext()
//                .build();
//    }

//    @Bean
//    @GrpcClient("reservation-service")
//    public ReservationServiceGrpc.ReservationServiceBlockingStub reservationServiceStub(ManagedChannel managedChannel) {
//        // Create and return the ReservationServiceBlockingStub bean
//        return ReservationServiceGrpc.newBlockingStub(managedChannel);
//    }
//
//    @Bean
//    @GrpcClient("accommodation-service")
//    public AccommodationServiceGrpc.AccommodationServiceBlockingStub accommodationServiceBlockingStub(ManagedChannel managedChannel) {
//        return AccommodationServiceGrpc.newBlockingStub(managedChannel);
//    }


//    @Bean
//    public ReservationService reservationService() {
//        // Create and return the ReservationService bean
//        return new ReservationService(reservationRepository, accommodationServiceBlockingStub);
//    }
}
