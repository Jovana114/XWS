package com.xws.user.service.impl;

import com.xws.user.entity.Rating;
import com.xws.user.repo.RatingRepository;
import com.xws.user.entity.User;
import com.xws.user.repo.UserRepository;
import com.xws.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }
    public ArrayList<Rating> getRatingsForHost(String hostId) {
        // Implementirajte logiku za dohvatanje ocena za određenog hosta (hostId)
        return ratingRepository.findByHostId(hostId);
    }


}
