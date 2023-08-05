package com.xws.user.repo;

import com.xws.user.entity.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccommodationRepository extends MongoRepository<Accommodation, String> {
}
