package com.example.Accommodationservice.repository;


import com.example.Accommodationservice.model.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface AccommodationRepository extends MongoRepository<Accommodation, String> {

}
