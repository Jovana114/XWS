package com.example.Accommodationservice.service;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.AppointmentRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.*;
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

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AccommodationService(AccommodationRepository accommodationRepository, ReservationRepository reservationRepository, AppointmentRepository appointmentRepository) {
        this.accommodationRepository = accommodationRepository;
        this.reservationRepository = reservationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void addReservationToAppointment(AddReservationToAppointmentRequest request, StreamObserver<Empty> responseObserver) {
        String appointmentId = request.getAppointmentId();
        com.xws.common.Reservation grpcReservation = request.getReservation();

        Timestamp timestamp = grpcReservation.getStartDate();
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1000000;
        Date startDate = new Date(milliseconds);

        Timestamp timestamp2 = grpcReservation.getEndDate();
        long milliseconds2 = timestamp2.getSeconds() * 1000 + timestamp2.getNanos() / 1000000;
        Date endDate = new Date(milliseconds2);

        Reservation reservation = new Reservation(grpcReservation.getId(), grpcReservation.getSourceUser(),
                grpcReservation.getAppointmentId(), startDate, endDate,
                grpcReservation.getNumGuests(), grpcReservation.getApproved());

        reservationRepository.save(reservation);

        for(Accommodation accommodation: accommodationRepository.findAll()){
            for(Appointments appointment: accommodation.getAppointments()){
                if(appointment.getId().equals(appointmentId)){

                    if (appointment.getReservations() == null) {
                        appointment.setReservations(new ArrayList<>());
                    }

                    appointment.getReservations().add(reservation);

                    appointmentRepository.save(appointment);
                    accommodationRepository.save(accommodation);

                }
            }
        }

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

            for(Appointments appointment: accommodation_found.getAppointments()) {

                for (Reservation reservation : appointment.getReservations()) {
                    if (reservation.getApproved() && reservation.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now())) {
                        hasActiveReservation = true;
                        break;
                    }
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