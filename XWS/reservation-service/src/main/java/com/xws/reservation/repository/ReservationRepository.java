package com.xws.reservation.repository;

import com.xws.reservation.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findBySourceUserAndIdAppointment(String suser, String idacc);

}