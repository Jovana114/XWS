package com.xws.user.repo;

import com.xws.user.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.xws.user.entity.Rating;
@Repository

public interface RatingRepository extends MongoRepository<Rating, String> {
}
