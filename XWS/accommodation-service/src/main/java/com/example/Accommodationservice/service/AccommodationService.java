package com.example.Accommodationservice.service;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.ZoneId;
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
        String source_user = request.getSourceUser();

        Timestamp timestamp = grpcReservation.getStartDate();
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1000000;
        Date startDate = new Date(milliseconds);

        Timestamp timestamp2 = grpcReservation.getEndDate(); // Changed to getEndDate() instead of getStartDate()
        long milliseconds2 = timestamp2.getSeconds() * 1000 + timestamp2.getNanos() / 1000000;
        Date endDate = new Date(milliseconds2);

        Reservation reservation = new Reservation(grpcReservation.getId(), grpcReservation.getSourceUser(),
                grpcReservation.getAccommodationId(), startDate, endDate,
                grpcReservation.getNumGuests(), grpcReservation.getApproved());

        reservationRepository.save(reservation);

        // Retrieve the user from the database based on userOwnerId
        Optional<Accommodation> _accommodation = accommodationRepository.findById(accommodationId);

        // Add the reservation to the user's list of reservations
        if (_accommodation.isPresent()) {
            Accommodation accommodation = _accommodation.get();

            // Check if reservations list is null, if so, initialize it to an empty ArrayList
            if (accommodation.getReservations() == null) {
                accommodation.setReservations(new ArrayList<>());
            }

            accommodation.getReservations().add(reservation);

            // Save the updated user to the database
            accommodationRepository.save(accommodation);
        }

        // Send an empty response
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void checkIfAccommodationHasActiveReservations(CheckIfAccommodationHasActiveReservationsRequest request, StreamObserver<CheckIfAccommodationHasActiveReservationsResponse> responseObserver) {
        com.xws.accommodation.Accommodation grpcAccommodation = request.getAccommodation();

        Optional<Accommodation> accommodation = accommodationRepository.findById(grpcAccommodation.getId());

        boolean hasActiveReservation = false;

        if(accommodation.isPresent()){
            Accommodation accommodation_found = accommodation.get();

            for(Reservation reservation: accommodation_found.getReservations()){
                if(reservation.getApproved() && reservation.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now())){
                    hasActiveReservation = true;
                    break;
                }
            }

        }

        CheckIfAccommodationHasActiveReservationsResponse response = CheckIfAccommodationHasActiveReservationsResponse.newBuilder()
                .setAccommodationHasActiveReservations(hasActiveReservation)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}