package com.xws.reservation;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.xws.proto.HelloRequest;
import com.xws.proto.HelloResponse;
import com.xws.proto.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoRepositories
public class ReservationApplication {
    public static void main(String[] args) {

        SpringApplication.run(ReservationApplication.class, args);

    }

}
