package com.xws.reservation.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.google.protobuf.Timestamp;
import com.xws.accommodation.*;
import com.xws.reservation.CreateReservationRequest;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.entity.Reservation;
import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationService reservationService;

    @PostMapping("/create_reservation/{appointment_id}/{source_user}")
    public ResponseEntity<?> create(@PathVariable("appointment_id") String appointment_id, @PathVariable("source_user") String source_user, @RequestBody Reservation reservation) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("reservation-service", 7575)
                .usePlaintext()
                .build();

        ReservationServiceGrpc.ReservationServiceBlockingStub stub = ReservationServiceGrpc.newBlockingStub(channel);

        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                .usePlaintext()
                .build();

        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub1 =
                AccommodationServiceGrpc.newBlockingStub(channel1);

        CheckIfAppointmentHasAutoApprovalRequest request1 = CheckIfAppointmentHasAutoApprovalRequest.newBuilder()
                .setAppointmentId(appointment_id)
                .build();

        try {

            CheckIfAppointmentHasAutoApprovalRequestResponse response = stub1.checkIfAppointmentHasAutoApproval(request1);

            if (!response.getAppointmentHasAutoApproval()) {

                Timestamp startDateTimestamp = convertToTimestamp(reservation.getStartDate());
                Timestamp endDateTimestamp = convertToTimestamp(reservation.getEndDate());

                com.xws.common.Reservation grpcReservation = com.xws.common.Reservation.newBuilder()
                        .setSourceUser(source_user)
                        .setAppointmentId(appointment_id)
                        .setStartDate(startDateTimestamp)
                        .setEndDate(endDateTimestamp)
                        .setNumGuests(reservation.getNumGuests())
                        .setApproved(false)
                        .setId(reservation.getId())
                        .build();

                CreateReservationRequest request = CreateReservationRequest.newBuilder()
                        .setAppointmentId(appointment_id)
                        .setReservation(grpcReservation)
                        .setSourceUser(source_user)
                        .build();

                try {
                    stub.createReservation(request);
                    return ResponseEntity.ok().body("{\"message\": \"Reservation request sent\"}");
                } catch (StatusRuntimeException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to create reservation\"}");
                }
            } else {

                Timestamp startDateTimestamp = convertToTimestamp(reservation.getStartDate());
                Timestamp endDateTimestamp = convertToTimestamp(reservation.getEndDate());

                com.xws.common.Reservation grpcReservation = com.xws.common.Reservation.newBuilder()
                        .setSourceUser(source_user)
                        .setAppointmentId(appointment_id)
                        .setStartDate(startDateTimestamp)
                        .setEndDate(endDateTimestamp)
                        .setNumGuests(reservation.getNumGuests())
                        .setApproved(true)
                        .setId(reservation.getId())
                        .build();

                CreateReservationRequest request = CreateReservationRequest.newBuilder()
                        .setAppointmentId(appointment_id)
                        .setReservation(grpcReservation)
                        .setSourceUser(source_user)
                        .build();

                try {
                    stub.createReservation(request);
                    return ResponseEntity.ok().body("{\"message\": \"Reservation request sent\"}");
                } catch (StatusRuntimeException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to create reservation\"}");
                }
            }
        } catch (StatusRuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to create reservation\"}");
        }
    }

    private Timestamp convertToTimestamp(Date date) {
        long seconds = date.getTime() / 1000;
        int nanos = (int) ((date.getTime() % 1000) * 1000000);
        return Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> allReservations()
    {
        List<Reservation> requests = reservationRepository.findAll();
        if(!requests.isEmpty())
            return new ResponseEntity<>(requests, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/remove_request/{res_id}")
    public ResponseEntity<String> delete(@PathVariable("res_id") String res_id) {
        Optional<Reservation> reservation = reservationRepository.findById(res_id);

        if (reservation.isPresent()) {
            Reservation reservation_found = reservation.get();

            if (!reservation_found.getApproved()) {
                try {
                    removeReservationFromServices(reservation_found);
                    reservationRepository.delete(reservation_found);
                    return ResponseEntity.ok("{\"message\": \"Reservation request removed\"}");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to remove reservation\"}");
                }
            } else if (reservation_found.getApproved() && java.time.Duration.between(java.time.LocalDateTime.now(), reservation_found.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()).toHours() < 24) {
                return new ResponseEntity<>("You cannot cancel 24h before the start date", HttpStatus.NOT_FOUND);
            } else if (reservation_found.getApproved() && java.time.Duration.between(java.time.LocalDateTime.now(), reservation_found.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()).toHours() > 24){
                try {
                    removeApprovedReservationFromServices(reservation_found);
                    reservationRepository.delete(reservation_found);
                    return ResponseEntity.ok("{\"message\": \"Reservation request removed\"}");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to remove reservation\"}");
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

    private void removeReservationFromServices(Reservation reservation) {
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub1 =
                AccommodationServiceGrpc.newBlockingStub(channel1);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("user-service", 6565)
                .usePlaintext()
                .build();
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        RemoveReservationRequest request = RemoveReservationRequest.newBuilder()
                .setReservationId(reservation.getId())
                .setAppointmentId(reservation.getIdAppointment())
                .build();

        RemoveReservationRequestUser request1 = RemoveReservationRequestUser.newBuilder()
                .setReservationId(reservation.getId())
                .setSourceUser(reservation.getSourceUser())
                .build();

        stub1.removeReservation(request);
        stub.removeReservationUser(request1);

        channel1.shutdown();
        channel.shutdown();
    }

    private void removeApprovedReservationFromServices(Reservation reservation) {
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                .usePlaintext()
                .build();
        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub1 =
                AccommodationServiceGrpc.newBlockingStub(channel1);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("user-service", 6565)
                .usePlaintext()
                .build();
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        RemoveApprovedReservationRequest request = RemoveApprovedReservationRequest.newBuilder()
                .setReservationId(reservation.getId())
                .setAppointmentId(reservation.getIdAppointment())
                .build();

        RemoveApprovedReservationRequestUser request1 = RemoveApprovedReservationRequestUser.newBuilder()
                .setReservationId(reservation.getId())
                .setSourceUser(reservation.getSourceUser())
                .build();

        stub1.removeApprovedReservation(request);
        stub.removeApprovedReservationRequest(request1);

        channel1.shutdown();
        channel.shutdown();
    }

    @PutMapping("/{suser}+{idacc}")
    public ResponseEntity<?> accept(@PathVariable("suser") String suser, @PathVariable("idacc") String idacc)
    {
        List<Reservation> reqList = reservationRepository.findBySourceUserAndIdAppointment(suser, idacc);
        if(!reqList.isEmpty()){
            Reservation newReservation = reqList.get(0);
            //newReservation.setState("Accepted");
            reservationRepository.save(newReservation);
            return new ResponseEntity<>(newReservation ,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}