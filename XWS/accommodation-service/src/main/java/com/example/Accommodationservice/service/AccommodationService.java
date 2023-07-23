package com.example.Accommodationservice.service;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddReservationToAccommodationRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.Optional;

@GrpcService
public class AccommodationService extends AccommodationServiceGrpc.AccommodationServiceImplBase {

    private final AccommodationRepository accommodationRepository;

    @Autowired
    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public void addReservationToAccommodation(AddReservationToAccommodationRequest request, StreamObserver<Empty> responseObserver) {
        String accommodationId = request.getAccommodationId();
        com.xws.common.Reservation grpcReservation = request.getReservation();

        Timestamp timestamp = grpcReservation.getStartDate();
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1000000;
        Date startDate = new Date(milliseconds);

        Timestamp timestamp2 = grpcReservation.getStartDate();
        long milliseconds2 = timestamp2.getSeconds() * 1000 + timestamp2.getNanos() / 1000000;
        Date endDate = new Date(milliseconds2);

        Reservation reservation = new Reservation(grpcReservation.getId(), grpcReservation.getSourceUser(),
                grpcReservation.getAccommodationId(), startDate, endDate,
                grpcReservation.getNumGuests(), grpcReservation.getApproved());

        // Retrieve the user from the database based on userOwnerId
        Optional<Accommodation> _accommodation = accommodationRepository.findById(accommodationId);

        // Add the reservation to the user's list of reservations
        if(_accommodation.isPresent()) {
            Accommodation accommodation = _accommodation.get();
            accommodation.getReservations().add(reservation);

            // Save the updated user to the database
            accommodationRepository.save(accommodation);
        }

        // Send an empty response
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
