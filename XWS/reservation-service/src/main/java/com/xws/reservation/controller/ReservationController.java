package com.xws.reservation.controller;

import java.util.List;

import com.xws.reservation.entity.Reservation;
import com.xws.reservation.repository.ReservationRepository;
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

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Reservation reservation) {
        Reservation newReservation = new Reservation(reservation.getSourceUser(), reservation.getIdAccommodation(),
                reservation.getStartDate(), reservation.getEndDate(), reservation.getNumGuests());

        reservationRepository.save(newReservation);

        return ResponseEntity.ok("Reservation request sent");
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
            newReservation.setState("Accepted");
            reservationRepository.save(newReservation);
            return new ResponseEntity<>(newReservation ,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}