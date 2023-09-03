package com.xws.user.controller;

import com.xws.accommodation.*;
import com.xws.user.entity.*;
import com.xws.user.entity.Accommodation;
import com.xws.user.entity.User;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.repo.AccommodationRepository;
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
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @GetMapping("/getUserById/{user_id}")
    public User getUserById(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        return user.get();
    }

    @GetMapping("/accommodations/{user_id}")
    public Set<Accommodation> getUserAccommodationsByUserId(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        return user.get().getAccommodations();
    }

    @GetMapping("/reservations/{user_id}")
    public Set<Reservation> getUserReservationsByUserId(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(!user.get().getReservations().isEmpty()){
            return user.get().getReservations();
        } else {
            return new HashSet<>();
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
                    if(!user_found.getReservations().isEmpty() && !user_found.getReservations().stream().allMatch(Objects::isNull)) {
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
                for (Role role : user_found.getRoles()) {
                    if (role.getName() == ERole.ROLE_GUEST) {
                        userRepository.delete(user_found);
                        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
                    } else if (role.getName() == ERole.ROLE_HOST){

                        for (Accommodation accommodation : user_found.getAccommodations()) {
                            accommodationRepository.delete(accommodation);
                        }

                        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("accommodation-service", 8585)
                                .usePlaintext()
                                .build();

                        AccommodationServiceGrpc.AccommodationServiceBlockingStub stub1 =
                                AccommodationServiceGrpc.newBlockingStub(channel1);

                        com.xws.accommodation.User grpcUser = com.xws.accommodation.User.newBuilder()
                                .setId(user_found.getId())
                                .build();

                        RemoveAccommodationsOfHostThatIsBeingDeletedRequest request = RemoveAccommodationsOfHostThatIsBeingDeletedRequest.newBuilder()
                                .setUser(grpcUser)
                                .build();

                        try {

                            stub1.removeAccommodationsOfHostThatIsBeingDeleted(request);

                        } catch (StatusRuntimeException e) {
                            Status status = Status.fromThrowable(e);
                            System.err.println("Error during gRPC call: " + status.getCode());
                        }

                        userRepository.delete(user_found);
                        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
                    }
                }
            } else {
                return ResponseEntity.ok(new MessageResponse("User has active reservations!"));
            }
        }
        return ResponseEntity.badRequest().body("User is not deleted!");
    }

}