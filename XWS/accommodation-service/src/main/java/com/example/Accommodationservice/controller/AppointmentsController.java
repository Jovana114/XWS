package com.example.Accommodationservice.controller;

import com.example.Accommodationservice.Response.MessageResponse;
import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    @PostMapping("/create/{accommodation_id}")
    public ResponseEntity<?> createApp(@PathVariable("accommodation_id") String accommodation_id, @RequestBody Appointments appointments) {
        try {
            Optional<Accommodation> accommodation = accommodationRepository.findById(accommodation_id);

            if(accommodation.isPresent()) {
                Accommodation _accommodation = accommodation.get();

                Appointments new_appointment = new Appointments(appointments.getStart(),
                        appointments.getEnd(), appointments.getReserved(), appointments.getPrice(),
                        appointments.getPrice_per(), appointments.getReservations());

                appointmentRepository.save(new_appointment);

                _accommodation.getAppointments().add(new_appointment);
                accommodationRepository.save(_accommodation);
            }
            return ResponseEntity.ok(new MessageResponse("Appointment created successfully!"));
        } catch (Exception e) {
            return ResponseEntity.ok(new MessageResponse("Failed!"));
        }
    }

    @PutMapping("/update/{app_id}")
    public ResponseEntity<Appointments> updateApp(@PathVariable("app_id") String app_id, @RequestBody Appointments appointment) {
        Optional<Appointments> appointment1 = appointmentRepository.findById(app_id);

        if (appointment1.isPresent()) {
            Appointments _appointment = appointment1.get();
            if(_appointment.getReserved() == false) {
                _appointment.setStart(appointment.getStart());
                _appointment.setEnd(appointment.getEnd());
                _appointment.setReserved(appointment.getReserved());
                _appointment.setPrice(appointment.getPrice());
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
