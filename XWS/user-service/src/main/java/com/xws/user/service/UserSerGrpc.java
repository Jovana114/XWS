package com.xws.user.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.xws.accommodation.AddAccommodationToUserOwner;
import com.xws.accommodation.AddReservationToUserRequest;
import com.xws.accommodation.ApprovingReservationChangeForUserRequest;
import com.xws.accommodation.UserServiceGrpc;
import com.xws.user.entity.Accommodation;
import com.xws.user.entity.Reservation;
import com.xws.user.entity.User;
import com.xws.user.repo.AccommodationRepository;
import com.xws.user.repo.ReservationRepository;
import com.xws.user.repo.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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
                user.setReservations(new ArrayList<>());
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
                user_found.setAccommodations(new ArrayList<>());
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

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.setApproved(true);
            reservationRepository.save(reservation);

            String appointment_id = reservation.getIdAppointment();

            for(Reservation reservation1: reservationRepository.findAll()){
                if(!reservation1.getApproved() && reservation1.getIdAppointment().equals(appointment_id)){
                    reservationRepository.delete(reservation1);
                }
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        }
    }
}