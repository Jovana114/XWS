package com.xws.reservation.controller;

import java.util.Date;
import java.util.List;

import com.google.protobuf.Timestamp;
import com.xws.reservation.CreateReservationRequest;
import com.xws.reservation.ReservationServiceGrpc;
import com.xws.reservation.entity.Reservation;
import com.xws.reservation.repository.ReservationRepository;
import com.xws.reservation.service.ReservationService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @PostMapping("/create_reservation/{accommodation_id}/{source_user}")
    public ResponseEntity<?> create(@PathVariable("accommodation_id") String accomodation_id, @PathVariable("source_user") String source_user, @RequestBody Reservation reservation) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("reservation-service", 7575)
                .usePlaintext()
                .build();

        ReservationServiceGrpc.ReservationServiceBlockingStub stub = ReservationServiceGrpc.newBlockingStub(channel);

        Timestamp startDateTimestamp = convertToTimestamp(reservation.getStartDate());
        Timestamp endDateTimestamp = convertToTimestamp(reservation.getEndDate());

        com.xws.common.Reservation grpcReservation = com.xws.common.Reservation.newBuilder()
                .setSourceUser(source_user)
                .setAccommodationId(accomodation_id)
                .setStartDate(startDateTimestamp)
                .setEndDate(endDateTimestamp)
                .setNumGuests(reservation.getNumGuests())
                .setApproved(reservation.getApproved())
                .build();

        CreateReservationRequest request = CreateReservationRequest.newBuilder()
                .setAccommodationId(accomodation_id)
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

    private Timestamp convertToTimestamp(Date date) {
        long seconds = date.getTime() / 1000;
        int nanos = (int) ((date.getTime() % 1000) * 1000000);
        return Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
    }

    @GetMapping()
    public ResponseEntity<?> allReservations()
    {
        List<Reservation> requests = reservationRepository.findAll();
        if(!requests.isEmpty())
            return new ResponseEntity<>(requests, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{suser}+{idacc}")
    public ResponseEntity<?> delete(@PathVariable("suser") String suser, @PathVariable("idacc") String idacc)
    {
        List<Reservation> reqList = reservationRepository.findBySourceUserAndIdAccommodation(suser, idacc);
        if(!reqList.isEmpty()){
            Reservation newReservation = reqList.get(0);
            reservationRepository.deleteById(newReservation.getId());
            return new ResponseEntity<>(newReservation ,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @PutMapping("/{suser}+{idacc}")
    public ResponseEntity<?> accept(@PathVariable("suser") String suser, @PathVariable("idacc") String idacc)
    {
        List<Reservation> reqList = reservationRepository.findBySourceUserAndIdAccommodation(suser, idacc);
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