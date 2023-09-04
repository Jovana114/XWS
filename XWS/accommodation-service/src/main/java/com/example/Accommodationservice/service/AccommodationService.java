package com.example.Accommodationservice.service;

import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.AppointmentRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Override
    public void checkIfAppointmentHasAutoApproval(CheckIfAppointmentHasAutoApprovalRequest request, StreamObserver<CheckIfAppointmentHasAutoApprovalRequestResponse> responseObserver) {
        String appointment_id = request.getAppointmentId();

        Optional<Appointments> appointment = appointmentRepository.findById(appointment_id);

        boolean has_auto_approval = false;

        if(appointment.isPresent()){
            Appointments appointment_found = appointment.get();
            if(appointment_found.isAuto_reservation()){
                has_auto_approval = true;

                appointment_found.setReserved(true);
                appointmentRepository.save(appointment_found);

            }
        }

        CheckIfAppointmentHasAutoApprovalRequestResponse response = CheckIfAppointmentHasAutoApprovalRequestResponse.newBuilder()
                .setAppointmentHasAutoApproval(has_auto_approval)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeReservation(RemoveReservationRequest request, StreamObserver<Empty> responseObserver) {
        String reservation_id = request.getReservationId();
        String appointment_id = request.getAppointmentId();
        Optional<Appointments> appointments = appointmentRepository.findById(appointment_id);
        Optional<Reservation> reservation = reservationRepository.findById(reservation_id);
        if(reservation.isPresent() && appointments.isPresent()){

            Reservation reservation_found = reservation.get();
            Appointments appointments_found = appointments.get();

            appointments_found.getReservations().remove(reservation_found);
            appointmentRepository.save(appointments_found);

            reservationRepository.delete(reservation_found);
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void removeApprovedReservation(RemoveApprovedReservationRequest request, StreamObserver<Empty> responseObserver) {
        String reservation_id = request.getReservationId();
        String appointment_id = request.getAppointmentId();
        Optional<Appointments> appointments = appointmentRepository.findById(appointment_id);
        Optional<Reservation> reservation = reservationRepository.findById(reservation_id);
        if(reservation.isPresent() && appointments.isPresent()){

            Reservation reservation_found = reservation.get();
            Appointments appointment_found = appointments.get();

            appointment_found.setReserved(false);
            appointment_found.getReservations().remove(reservation_found);
            appointmentRepository.save(appointment_found);

            reservationRepository.delete(reservation_found);
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    public List<Accommodation> findAccommodationsByPriceRange(Long userId, Long priceMin, Long priceMax) {
        return accommodationRepository.findByUserIdAndPriceBetween(userId, priceMin, priceMax);
    }
}