package com.example.Accommodationservice.repository;


import com.example.Accommodationservice.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingRepository extends MongoRepository<Rating, Long> {
}