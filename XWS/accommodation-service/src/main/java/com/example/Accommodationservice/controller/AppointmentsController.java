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

    @GetMapping("/appointments_per_user/{user_id}")
    public List<Appointments> getAppointmentsUserId(@PathVariable("user_id") String user_id){
        List<Appointments> appointments = new ArrayList<>();
        for(Accommodation accommodation: accommodationRepository.findAll()){
            if(accommodation.getUser_id().equals(user_id)) {
                for (Appointments appointment : accommodation.getAppointments()) {
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    @GetMapping("/{appointment_id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointment_id") String appointment_id) {
        try {
            Optional<Appointments> appointments = appointmentRepository.findById(appointment_id);
            if (appointments.isPresent()) {
                return ResponseEntity.ok(appointments.get());
            } else
                return ResponseEntity.badRequest().body("Appointment Not Found");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
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
                _appointment.setPrice(appointment.getPrice());
                _appointment.setPrice_per(appointment.getPrice_per());
                _appointment.setPrice_type(appointment.getPrice_type());
                _appointment.setAuto_reservation(appointment.isAuto_reservation());
                appointmentRepository.save(_appointment);
                return new ResponseEntity<>(HttpStatus.OK);
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all_appointemnts")
    public List<Appointments> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") String id){
        try{
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok("Appointment Successfully Deleted.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}