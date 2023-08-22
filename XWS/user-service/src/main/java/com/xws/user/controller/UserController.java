package com.xws.user.controller;

import com.xws.accommodation.AccommodationServiceGrpc;
import com.xws.accommodation.CheckIfAccommodationHasActiveReservationsRequest;
import com.xws.accommodation.CheckIfAccommodationHasActiveReservationsResponse;
import com.xws.accommodation.UserServiceGrpc;
import com.xws.user.entity.*;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.repo.RatingRepository;
import com.xws.user.repo.ReservationRepository;
import com.xws.user.repo.UserRepository;
import com.xws.user.service.UserService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUserById/{user_id}")
    public User getUserById(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        return user.get();
    }

    @GetMapping("/accommodations/{user_id}")
    public List<Accommodation> getUserAccommodationsByUserId(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        return user.get().getAccommodations();
    }

    @GetMapping("/reservations/{user_id}")
    public List<Reservation> getUserReservationsByUserId(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(!user.get().getReservations().isEmpty()){
            return user.get().getReservations();
        } else {
            return new ArrayList<>();
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @PutMapping("/password/{userId}/{password}")
    public ResponseEntity<?> updatePassword(@PathVariable String userId, @PathVariable String password) {
        userService.updatePassword(userId, password);
        return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
    }


    @DeleteMapping("/delete_user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User user_found = user.get();

            boolean canDeleteUser = true;

            for (Role role : user_found.getRoles()) {
                if (role.getName() == ERole.ROLE_GUEST) {
                    if(!user_found.getReservations().isEmpty()) {
                        for (Reservation reservation : user_found.getReservations()) {
                            if (reservation.getApproved() && reservation.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now())) {
                                canDeleteUser = false;
                                break;
                            }
                        }
                    } else {
                        canDeleteUser = true;
                        break;
                    }
                } else if (role.getName() == ERole.ROLE_HOST) {
                    if(!user_found.getAccommodations().isEmpty()) {
                        for (Accommodation accommodation : user_found.getAccommodations()) {
                            ManagedChannel channel1 = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                                    .usePlaintext()
                                    .build();

                            AccommodationServiceGrpc.AccommodationServiceBlockingStub stub1 =
                                    AccommodationServiceGrpc.newBlockingStub(channel1);

                            com.xws.accommodation.Accommodation grpcAccommodation = com.xws.accommodation.Accommodation.newBuilder()
                                    .setId(accommodation.getId())
                                    .setName(accommodation.getName())
                                    .build();

                            CheckIfAccommodationHasActiveReservationsRequest checkIfAccommodationHasActiveReservationsRequest = CheckIfAccommodationHasActiveReservationsRequest.newBuilder()
                                    .setAccommodation(grpcAccommodation)
                                    .build();

                            try {
                                CheckIfAccommodationHasActiveReservationsResponse response = stub1.checkIfAccommodationHasActiveReservations(checkIfAccommodationHasActiveReservationsRequest);

                                if (response.getAccommodationHasActiveReservations()) {
                                    canDeleteUser = false;
                                    break;
                                }

                            } catch (StatusRuntimeException e) {
                                Status status = Status.fromThrowable(e);
                                System.err.println("Error during gRPC call: " + status.getCode());
                            }

                        }
                    } else {
                        canDeleteUser = true;
                    }
                }
            }

            if (canDeleteUser) {
                userRepository.delete(user_found);
                return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
            } else {
                return ResponseEntity.ok(new MessageResponse("User has active reservations!"));
            }
        }
        return ResponseEntity.badRequest().body("User is not deleted!");
    }

    @PostMapping("/rate_host/{reservation_id}/{guest_id}/{host_id}/{rating}")
    public ResponseEntity<String> rateHost(@PathVariable("reservation_id") String reservationId, @PathVariable("guest_id") String guestId, @PathVariable("host_id") String hostId, @PathVariable("rating") int rating) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();

            if (reservation.getSourceUser().equals(guestId)) {
                if (reservation.getHostId().equals(hostId)) {
                    if (!reservation.getApproved()) {
                        Rating newRating = new Rating();
                        newRating.setGuestId(Integer.parseInt(guestId));
                        newRating.setHostId(Integer.parseInt(hostId));
                        newRating.setRatingValue(rating);

                        ratingRepository.save(newRating);

                        System.out.println("Guest " + guestId + " rated Host " + hostId + " with a rating of " + rating);

                        return ResponseEntity.ok("{\"message\": \"Rating added successfully.\"}");
                    } else {
                        return ResponseEntity.badRequest().body("{\"error\": \"You cannot rate a reservation that has been approved.\"}");
                    }
                } else {
                    return ResponseEntity.badRequest().body("{\"error\": \"This reservation is not for the specified host.\"}");
                }
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"You are not authorized to rate this reservation.\"}");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}