package com.example.Accommodationservice.controller;

import com.example.Accommodationservice.Response.MessageResponse;
import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.service.AccommodationService;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddAccommodationToUserOwner;
import com.xws.accommodation.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        Accommodation new_accommodation = new Accommodation(accommodation.getId(), accommodation.getName(),
                accommodation.getLocation(), accommodation.getBenefits(),
                accommodation.getMin_guests(), accommodation.getMax_guests(),
                accommodation.getAppointments(), accommodation.getReservations());

        new_accommodation.setUser_id(user_id);
        accommodationRepository.save(new_accommodation);

        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("user-service", 6565)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub1 =
                UserServiceGrpc.newBlockingStub(channel1);

        com.xws.accommodation.Accommodation grpcAccommodation = com.xws.accommodation.Accommodation.newBuilder()
                .setId(accommodation.getId())
                .setName(accommodation.getName())
                .build();

        AddAccommodationToUserOwner grpcRequest = AddAccommodationToUserOwner.newBuilder()
                .setAccommodation(grpcAccommodation)
                .setUserOwnerId(user_id)
                .build();

        try {
            stub1.addAccommodationToUser(grpcRequest);
            return ResponseEntity.ok().body("Accommodation created successfully!");
        } catch (StatusRuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create accommodation");
        }
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

    @GetMapping(value = "/{location}/{location2}")
    public ResponseEntity<InputStreamResource> getImageDynamicType(@PathVariable("location") String location, @PathVariable("location2") String location2) throws FileNotFoundException {
        MediaType contentType = MediaType.IMAGE_JPEG;
        String fileLocation = location + "//" + location2;
        InputStream in = new FileInputStream(fileLocation);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }

    @PutMapping(value = "/add_image/{id}/image")
    public ResponseEntity<Accommodation> updateAccommdationImage(@PathVariable("id") String accommodation_id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(accommodation_id);
        if (accommodationOptional.isPresent() && !file.isEmpty()) {
            Accommodation accommodation_found = accommodationOptional.get();

            String fileName = accommodation_id + ".jpg";
            String imagesDirectoryPath = "images";

            Files.createDirectories(Paths.get(imagesDirectoryPath));

            String filePath = imagesDirectoryPath + "/" + fileName;
            File newImage = new File(filePath);
            try (OutputStream outputStream = new FileOutputStream(newImage)) {
                outputStream.write(file.getBytes());
            }

            accommodation_found.setPic("/" + filePath);
            accommodationRepository.save(accommodation_found);

            return new ResponseEntity<>(accommodation_found, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}