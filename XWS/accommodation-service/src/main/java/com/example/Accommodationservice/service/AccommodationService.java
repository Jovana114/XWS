package com.example.Accommodationservice.service;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddReservationToAccommodationRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;


import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@GrpcService
@ComponentScan(basePackages = "com.example.Accommodationservice.repository")
public class AccommodationService extends AccommodationServiceGrpc.AccommodationServiceImplBase {

    private final AccommodationRepository accommodationRepository;

    private final ReservationRepository reservationRepository;

    @Autowired
    public AccommodationService(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void addReservationToAccommodation(AddReservationToAccommodationRequest request, StreamObserver<Empty> responseObserver) {
        String accommodationId = request.getAccommodationId();
        com.xws.common.Reservation grpcReservation = request.getReservation();

        Timestamp timestamp = grpcReservation.getStartDate();
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1000000;
        Date startDate = new Date(milliseconds);

        Timestamp timestamp2 = grpcReservation.getEndDate();
        long milliseconds2 = timestamp2.getSeconds() * 1000 + timestamp2.getNanos() / 1000000;
        Date endDate = new Date(milliseconds2);

        Reservation reservation = new Reservation(grpcReservation.getId(), grpcReservation.getSourceUser(),
                grpcReservation.getAccommodationId(), startDate, endDate,
                grpcReservation.getNumGuests(), grpcReservation.getApproved());

        reservationRepository.save(reservation);


        Optional<Accommodation> _accommodation = accommodationRepository.findById(accommodationId);


        if (_accommodation.isPresent()) {
            Accommodation accommodation = _accommodation.get();


            if (accommodation.getReservations() == null) {
                accommodation.setReservations(new ArrayList<>());
            }

            accommodation.getReservations().add(reservation);


            accommodationRepository.save(accommodation);
        }


        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

}

