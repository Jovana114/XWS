package com.example.Accommodationservice.repository;

import com.example.Accommodationservice.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
