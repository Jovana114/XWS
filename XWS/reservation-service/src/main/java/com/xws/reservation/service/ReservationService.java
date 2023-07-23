package com.xws.reservation.service;


import com.xws.proto.HelloRequest;
import com.xws.proto.HelloResponse;
import com.xws.proto.HelloServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReservationService{
    @Autowired
    ReservationRepository reservationRepository;


}