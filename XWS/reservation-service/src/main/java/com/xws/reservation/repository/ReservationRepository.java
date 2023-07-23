package com.xws.reservation.repository;

import com.xws.reservation.entity.Reservation;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan("com.xws.reservation.service")
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findBySourceUserAndIdAccommodation(String suser, String idacc);

}