package com.xws.user.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.*;
import com.xws.user.entity.Accommodation;
import com.xws.user.entity.Reservation;
import com.xws.user.entity.User;
import com.xws.user.repo.AccommodationRepository;
import com.xws.user.repo.ReservationRepository;
import com.xws.user.repo.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.hc.core5.http2.H2ConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.*;

@GrpcService
@ComponentScan(basePackages = "com.xws.user.repository")
public class UserSerGrpc extends UserServiceGrpc.UserServiceImplBase {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final AccommodationRepository accommodationRepository;

    @Autowired
    public UserSerGrpc(ReservationRepository reservationRepository, UserRepository userRepository, AccommodationRepository accommodationRepository){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public void addReservationToUser(AddReservationToUserRequest request, StreamObserver<Empty> responseObserver) {
        com.xws.common.Reservation grpcReservation = request.getReservation();
        String source_user  = request.getSourceUser();

        Timestamp timestamp = grpcReservation.getStartDate();
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1000000;
        Date startDate = new Date(milliseconds);

        Timestamp timestamp2 = grpcReservation.getEndDate();
        long milliseconds2 = timestamp2.getSeconds() * 1000 + timestamp2.getNanos() / 1000000;
        Date endDate = new Date(milliseconds2);

        Reservation reservation = new Reservation(grpcReservation.getId(), grpcReservation.getSourceUser(),
                grpcReservation.getAppointmentId(), startDate, endDate,
                grpcReservation.getNumGuests(), grpcReservation.getApproved());

        reservationRepository.save(reservation);

        Optional<User> _user = userRepository.findById(source_user);

        if (_user.isPresent()) {
            User user = _user.get();

            if (user.getReservations() == null) {
                user.setReservations(new HashSet<>());
            }

            user.getReservations().add(reservation);

            userRepository.save(user);
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void addAccommodationToUser(AddAccommodationToUserOwner request, StreamObserver<Empty> responseObserver) {
        com.xws.accommodation.Accommodation grpcAccommodation = request.getAccommodation();
        String user_owner_id = request.getUserOwnerId();

        Accommodation accommodation = new Accommodation(grpcAccommodation.getId(), grpcAccommodation.getName());
        accommodationRepository.save(accommodation);

        Optional<User> user = userRepository.findById(user_owner_id);

        if(user.isPresent()){
            User user_found = user.get();

            if(user_found.getAccommodations() == null){
                user_found.setAccommodations(new HashSet<>());
            }

            user_found.getAccommodations().add(accommodation);
            userRepository.save(user_found);
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void approvingReservationChangeForUser(ApprovingReservationChangeForUserRequest request, StreamObserver<Empty> responseObserver) {
        String reservationId = request.getReservationId();

        try {
            Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

            if (reservationOptional.isPresent()) {
                Reservation approvedReservation = reservationOptional.get();
                String appointmentId = approvedReservation.getIdAppointment();

                List<Reservation> reservationsToDelete = new ArrayList<>();
                for (Reservation r : reservationRepository.findAll()) {
                    if (r.getIdAppointment().equals(appointmentId)) {
                        if (r.getId().equals(reservationId)) {
                            r.setApproved(true);
                        } else {
                            reservationsToDelete.add(r);
                        }
                    }
                }

                reservationRepository.save(approvedReservation);
                reservationRepository.deleteAll(reservationsToDelete);

                responseObserver.onNext(Empty.getDefaultInstance());
            } else {
                responseObserver.onNext(Empty.getDefaultInstance());
            }
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }


    @Override
    public void removeReservationUser(RemoveReservationRequestUser request, StreamObserver<Empty> responseObserver) {
        String reservation_id = request.getReservationId();
        String source_user = request.getSourceUser();

        Optional<User> user = userRepository.findById(source_user);
        Optional<Reservation> reservation = reservationRepository.findById(reservation_id);

        if (reservation.isPresent() && user.isPresent()) {
            Reservation reservation_found = reservation.get();
            User user_found = user.get();

            reservationRepository.delete(reservation_found);

            user_found.getReservations().removeIf(r -> r.getId().equals(reservation_id));

            userRepository.save(user_found);
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }



    @Override
    public void removeApprovedReservationRequest(RemoveApprovedReservationRequestUser request, StreamObserver<Empty> responseObserver) {
        String reservation_id = request.getReservationId();
        String source_user = request.getSourceUser();

        Optional<User> user = userRepository.findById(source_user);
        Optional<Reservation> reservation = reservationRepository.findById(reservation_id);

        if (reservation.isPresent() && user.isPresent()) {
            Reservation reservation_found = reservation.get();
            User user_found = user.get();

            user_found.setCancellation_number(user_found.getCancellation_number() + 1);
            user_found.getReservations().remove(reservation_found);
            userRepository.save(user_found);

            reservationRepository.delete(reservation_found);
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersCancellationNumber(getUsersCancellationNumberRequest request, StreamObserver<getUsersCancellationNumberResponse> responseObserver) {
        String id = request.getUser().getId();
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User user_found = user.get();

            getUsersCancellationNumberResponse response = getUsersCancellationNumberResponse.newBuilder()
                    .setUsersCancellationNumber(user_found.getCancellation_number())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}