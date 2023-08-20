package com.example.Accommodationservice.controller;

import com.example.Accommodationservice.Response.MessageResponse;
import com.example.Accommodationservice.model.*;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.AppointmentRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @PostMapping("/create/{accommodation_id}")
    public ResponseEntity<?> createApp(@PathVariable("accommodation_id") String accommodation_id, @RequestBody Appointments appointments) {
        try {
            Optional<Accommodation> accommodation = accommodationRepository.findById(accommodation_id);

            if (accommodation.isPresent()) {
                Accommodation _accommodation = accommodation.get();

                EPrice priceType = EPrice.valueOf(appointments.getPrice_type().toString());
                EPricePer pricePer = EPricePer.valueOf(appointments.getPrice_per().toString());

                Appointments new_appointment = new Appointments(
                        appointments.getStart(),
                        appointments.getEnd(),
                        false,
                        priceType,
                        pricePer,
                        new ArrayList<>(),
                        appointments.isAuto_reservation(),
                        appointments.getPrice()
                );

                appointmentRepository.save(new_appointment);

                _accommodation.getAppointments().add(new_appointment);
                accommodationRepository.save(_accommodation);

                return ResponseEntity.ok(new MessageResponse("Appointment created successfully!"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid enum value provided!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed!"));
        }
    }


    @PutMapping("/update/{app_id}")
    public ResponseEntity<Appointments> updateApp(@PathVariable("app_id") String app_id, @RequestBody Appointments appointment) {
        Optional<Appointments> appointment1 = appointmentRepository.findById(app_id);

        if (appointment1.isPresent()) {
            Appointments _appointment = appointment1.get();
            if(!_appointment.getReserved()) {
                _appointment.setStart(appointment.getStart());
                _appointment.setEnd(appointment.getEnd());
                _appointment.setReserved(appointment.getReserved());
                _appointment.setPrice(appointment.getPrice());
                _appointment.setPrice_per(appointment.getPrice_per());
                _appointment.setAuto_reservation(appointment.isAuto_reservation());
                _appointment.setPrice(appointment.getPrice());
                return new ResponseEntity<>(appointmentRepository.save(_appointment), HttpStatus.OK);
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all_appointemnts")
    public List<Appointments> getAllAppointments(){
        return appointmentRepository.findAll();
    }

}