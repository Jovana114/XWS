package com.example.Accommodationservice.controller;

import com.example.Accommodationservice.Response.MessageResponse;
import com.example.Accommodationservice.model.Accommodation;
import com.example.Accommodationservice.model.Appointments;
import com.example.Accommodationservice.model.Reservation;
import com.example.Accommodationservice.repository.AccommodationRepository;
import com.example.Accommodationservice.repository.AppointmentRepository;
import com.example.Accommodationservice.repository.ReservationRepository;
import com.example.Accommodationservice.service.AccommodationService;
import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.AddAccommodationToUserOwner;
import com.xws.accommodation.ApprovingReservationChangeForUserRequest;
import com.xws.accommodation.UserServiceGrpc;
import com.xws.reservation.ApprovingReservationRequest;
import com.xws.reservation.ReservationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accommodation")
public class AccommodationControler {

    @Autowired
    AccommodationRepository accommodationRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @PostMapping("/create/{user_id}")
    public ResponseEntity<?> create(@PathVariable("user_id") String user_id, @RequestBody Accommodation accommodation) {

        Accommodation new_accommodation = new Accommodation(accommodation.getId(), accommodation.getName(),
                accommodation.getLocation(), accommodation.getBenefits(),
                accommodation.getMin_guests(), accommodation.getMax_guests(),
                accommodation.getAppointments());

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

    @GetMapping("/search/accommodations")
    public List<Accommodation> searchAccommodations(@RequestParam String location, @RequestParam int numGuests,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date start,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date end) {

        List<Accommodation> filtered = new ArrayList<>();

        for (Accommodation accommodation : accommodationRepository.findAll()) {
            if (accommodation.getLocation().equals(location)
                    && accommodation.getMin_guests() <= numGuests
                    && accommodation.getMax_guests() >= numGuests
                    && isWithinAppointmentTime(accommodation, start, end)) {
                filtered.add(accommodation);
            }
        }

        return filtered;
    }

    private boolean isWithinAppointmentTime(Accommodation accommodation, Date start, Date end) {
        for (Appointments appointment : accommodation.getAppointments()) {
            if(!appointment.getReserved()) {
                Date appointmentStart = appointment.getStart();
                Date appointmentEnd = appointment.getEnd();

                if (start.compareTo(appointmentEnd) < 0 && end.compareTo(appointmentStart) > 0 &&
                        end.compareTo(appointmentEnd) <= 0 && start.compareTo(appointmentStart) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @PutMapping("/approvingRequests/{app_id}_{res_id}")
    public ResponseEntity<Accommodation> approvingRequests(@PathVariable("app_id") String app_id, @PathVariable("res_id") String res_id) {
        Optional<Appointments> appointment = appointmentRepository.findById(app_id);

        if (appointment.isPresent()) {
            Appointments appointment_found = appointment.get();

            if(!appointment_found.isAuto_reservation()) {

                List<Reservation> reservations = appointment_found.getReservations();
                Optional<Reservation> chosenReservationOptional = reservations.stream()
                        .filter(reservation -> reservation.getId().equals(res_id))
                        .findFirst();

                if (chosenReservationOptional.isPresent()) {
                    Reservation chosenReservation = chosenReservationOptional.get();

                    for (Reservation reservation : reservations) {
                        if (!reservation.getId().equals(res_id)) {
                            appointment_found.getReservations().remove(reservation);
                            reservationRepository.delete(reservation);
                        }
                    }

                    chosenReservation.setApproved(true);
                    reservationRepository.save(chosenReservation);

                    appointment_found.setReserved(true);
                    appointmentRepository.save(appointment_found);

                    ManagedChannel channel = ManagedChannelBuilder.forAddress("user-service", 6565)
                            .usePlaintext()
                            .build();

                    ManagedChannel channel1 = ManagedChannelBuilder.forAddress("reservation-service", 7575)
                            .usePlaintext()
                            .build();

                    ReservationServiceGrpc.ReservationServiceBlockingStub stub1 =
                            ReservationServiceGrpc.newBlockingStub(channel1);

                    UserServiceGrpc.UserServiceBlockingStub stub =
                            UserServiceGrpc.newBlockingStub(channel);


                    ApprovingReservationRequest request1 = ApprovingReservationRequest.newBuilder()
                            .setReservationId(res_id)
                            .build();

                    ApprovingReservationChangeForUserRequest request = ApprovingReservationChangeForUserRequest.newBuilder()
                            .setReservationId(res_id)
                            .build();

                    try {
                        stub.approvingReservationChangeForUser(request);
                        stub1.approvingReservation(request1);
                    } catch (StatusRuntimeException e) {
                        // Handle any errors or exceptions that occur while calling the User Service
                        // You can choose to retry, log, or handle the error based on your application's requirements
                    }

                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}