package com.xws.reservation.service;


import com.google.protobuf.Empty;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddReservationToAppointmentRequest;
import com.xws.accommodation.AddReservationToUserRequest;
import com.xws.accommodation.UserServiceGrpc;
import com.xws.reservation.ApprovingReservationRequest;
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
import java.util.Optional;

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

        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("user-service", 6565)
                .usePlaintext()
                .build();

        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub =
                AccommodationServiceGrpc.newBlockingStub(channel);

        UserServiceGrpc.UserServiceBlockingStub stub1 =
                UserServiceGrpc.newBlockingStub(channel1);

        String appointmentId = request.getAppointmentId();
        com.xws.common.Reservation grpcReservation = request.getReservation();
        String source_user = request.getSourceUser();

        Reservation reservation = new Reservation(
                grpcReservation.getSourceUser(),
                grpcReservation.getAppointmentId(),
                new Date(grpcReservation.getStartDate().getSeconds() * 1000),
                new Date(grpcReservation.getEndDate().getSeconds() * 1000),
                grpcReservation.getNumGuests(),
                grpcReservation.getApproved()
        );

        reservationRepository.save(reservation);

        grpcReservation.newBuilderForType().setId(reservation.getId());

        AddReservationToAppointmentRequest grpcRequest = AddReservationToAppointmentRequest.newBuilder()
                .setAppointmentId(appointmentId)
                .setReservation(grpcReservation)
                .setSourceUser(source_user)
                .build();

        AddReservationToUserRequest grpcRequest1 = AddReservationToUserRequest.newBuilder()
                .setAppointmentId(appointmentId)
                .setReservation(grpcReservation)
                .setSourceUser(source_user)
                .build();

        try {
            stub.addReservationToAppointment(grpcRequest);
            stub1.addReservationToUser(grpcRequest1);
        } catch (StatusRuntimeException e) {
            // Handle any errors or exceptions that occur while calling the User Service
            // You can choose to retry, log, or handle the error based on your application's requirements
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void approvingReservation(ApprovingReservationRequest request, StreamObserver<Empty> responseObserver) {
        String reservationId = request.getReservationId();

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.setApproved(true);
            reservationRepository.save(reservation);

            String appointment_id = reservation.getIdAppointment();

            for(Reservation reservation1: reservationRepository.findAll()){
                if(!reservation1.getApproved() && reservation1.getIdAppointment().equals(appointment_id)){
                    reservationRepository.delete(reservation1);
                }
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        }
    }
}