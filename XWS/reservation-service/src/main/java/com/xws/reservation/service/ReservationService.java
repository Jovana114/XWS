package com.xws.reservation.service;


import com.google.protobuf.Empty;
import com.xws.reservation.ApprovingReservationRequest;
import com.xws.reservation.entity.Reservation;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.ComponentScan;

import java.util.Optional;

@GrpcService
@ComponentScan("com.xws.reservation.repository")
public class ReservationService extends ReservationServiceGrpc.ReservationServiceImplBase {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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