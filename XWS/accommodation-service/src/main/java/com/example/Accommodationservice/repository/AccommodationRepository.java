package com.example.Accommodationservice.repository;


import com.example.Accommodationservice.model.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccommodationRepository extends MongoRepository<Accommodation, String> {
}
