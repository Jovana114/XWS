package com.example.Accommodationservice.repository;


import com.example.Accommodationservice.model.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccommodationRepository extends MongoRepository<Accommodation, String> {

    List<Accommodation> findByUserIdAndPriceBetween(Long userId, Long priceMin, Long priceMax);
}
