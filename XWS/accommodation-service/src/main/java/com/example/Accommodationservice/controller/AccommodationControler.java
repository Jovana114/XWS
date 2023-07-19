package com.example.Accommodationservice.controller;

import com.example.Accommodationservice.Response.MessageResponse;
import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.repository.AccommodationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accommodation")
public class AccommodationControler {

    @Autowired
    AccommodationRepository accommodationRepository;

    @PostMapping("/create/{user_id}")
    public ResponseEntity<?> create(@PathVariable("user_id") String user_id, @RequestBody Accommodation accommodation) {

        Accommodation new_accommodation = new Accommodation(accommodation.getName(),
                accommodation.getLocation(), accommodation.getBenefits(),
                accommodation.getMin_guests(), accommodation.getMax_guests(),
                accommodation.getAppointments());

        new_accommodation.setUser_id(user_id);
        accommodationRepository.save(new_accommodation);

        return ResponseEntity.ok(new MessageResponse("Accommodation created successfully!"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> allAccommodation() {
        List<Accommodation> accommodations = accommodationRepository.findAll();
        if(!accommodations.isEmpty())
            return new ResponseEntity<>(accommodations, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{acc_id}")
    public ResponseEntity<Accommodation> updateAcc(@PathVariable("acc_id") String acc_id, @RequestBody Accommodation accommodation) {
        Optional<Accommodation> accommodation1 = accommodationRepository.findById(acc_id);

        if (accommodation1.isPresent()) {
            Accommodation _accommodation = accommodation1.get();
                _accommodation.setName(accommodation.getName());
                _accommodation.setBenefits(accommodation.getBenefits());
                _accommodation.setLocation(accommodation.getLocation());
                _accommodation.setMin_guests(accommodation.getMin_guests());
                _accommodation.setMax_guests(accommodation.getMax_guests());
                return new ResponseEntity<>(accommodationRepository.save(_accommodation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update_with_photo/{acc_id}")
    public ResponseEntity<Accommodation> Photo(@RequestParam("file") MultipartFile file, @PathVariable("acc_id") String acc_id) {
        Optional<Accommodation> accommodation1 = accommodationRepository.findById(acc_id);

        if (accommodation1.isPresent()) {
            Accommodation _accommodation = accommodation1.get();
            try {
                _accommodation.setPic(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(accommodationRepository.save(_accommodation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}