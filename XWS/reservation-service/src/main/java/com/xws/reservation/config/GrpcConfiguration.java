package com.xws.reservation.config;


import com.xws.proto.HelloRequest;
import com.xws.proto.HelloResponse;
import com.xws.proto.HelloServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcConfiguration {


//    @Bean
//    public Server grpcServer() throws IOException {
//        Server server = ServerBuilder.forPort(6565)
//                .addService(new AccommodationServiceGrpc(accommodationRepository))
//                .build();
//        server.start();
//        return server;
//    }
//}
//
//
//    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
//            .usePlaintext()
//            .build();
//
//    HelloServiceGrpc.HelloServiceBlockingStub stub
//            = HelloServiceGrpc.newBlockingStub(channel);
//
//    HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
//            .setFirstName("Baeldung")
//            .setLastName("gRPC")
//            .build());
//
//
//        System.out.println(helloResponse.toString());
//                channel.shutdown();

}