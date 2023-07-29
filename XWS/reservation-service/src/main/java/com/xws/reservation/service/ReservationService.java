package com.xws.reservation.service;


import com.google.protobuf.Empty;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddReservationToAccommodationRequest;
import com.xws.proto.HelloRequest;
import com.xws.proto.HelloResponse;
import com.xws.proto.HelloServiceGrpc;
import com.xws.reservation.CreateReservationRequest;
import com.xws.reservation.entity.Reservation;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@GrpcService
@ComponentScan("com.xws.reservation.repository")
public class ReservationService extends ReservationServiceGrpc.ReservationServiceImplBase {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<Empty> responseObserver) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                .usePlaintext()
                .build();

        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub =
                AccommodationServiceGrpc.newBlockingStub(channel);

        String accommodationId = request.getAccommodationId();
        com.xws.common.Reservation grpcReservation = request.getReservation();

        // Create a Reservation object from the received gRPC Reservation
        Reservation reservation = new Reservation(
                grpcReservation.getId(),
                grpcReservation.getSourceUser(),
                grpcReservation.getAccommodationId(),
                new Date(grpcReservation.getStartDate().getSeconds() * 1000),
                new Date(grpcReservation.getEndDate().getSeconds() * 1000),
                grpcReservation.getNumGuests(),
                grpcReservation.getApproved()
        );

        // Save the reservation to the database using the ReservationRepository
        reservationRepository.save(reservation);

        // Add the reservation to the user's list of reservations in the Service
        AddReservationToAccommodationRequest grpcRequest = AddReservationToAccommodationRequest.newBuilder()
                .setAccommodationId(accommodationId)
                .setReservation(grpcReservation)
                .build();

        try {
            stub.addReservationToAccommodation(grpcRequest);
        } catch (StatusRuntimeException e) {
            // Handle any errors or exceptions that occur while calling the User Service
            // You can choose to retry, log, or handle the error based on your application's requirements
        }

        // Send an empty response
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}