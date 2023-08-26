package com.xws.reservation.service;


import com.google.protobuf.Empty;
import com.xws.reservation.ApprovingReservationRequest;
import com.xws.reservation.entity.Reservation;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.repository.ReservationRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
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

        try {
            Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

            if (reservationOptional.isPresent()) {
                Reservation reservation = reservationOptional.get();
                reservation.setApproved(true);
                reservationRepository.save(reservation);

                String appointmentId = reservation.getIdAppointment();

                List<Reservation> reservationsToDelete = new ArrayList<>();
                for (Reservation r : reservationRepository.findAll()) {
                    if (!r.getApproved() && r.getIdAppointment().equals(appointmentId)) {
                        reservationsToDelete.add(r);
                    }
                }
                reservationRepository.deleteAll(reservationsToDelete);
            }

            responseObserver.onNext(Empty.getDefaultInstance());
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }

}