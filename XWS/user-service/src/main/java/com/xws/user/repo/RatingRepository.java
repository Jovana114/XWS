package com.xws.user.repo;

import com.xws.user.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.xws.user.entity.Rating;

import java.util.ArrayList;
import java.util.Optional;

@Repository

public interface RatingRepository extends MongoRepository<Rating, String> {
    ArrayList<Rating> findByHostId(String hostId);

    Optional<Rating> findByUserId(String id);

}
