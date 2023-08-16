package com.xws.user.service.impl;

import com.xws.user.entity.User;
import com.xws.user.repo.UserRepository;
import com.xws.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();

        // Update user's information
        user.setFirst_name(updatedUser.getFirst_name());
        user.setLast_name(updatedUser.getLast_name());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setAddress(updatedUser.getAddress());

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(String userId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();

        // Update user's information
        user.setPassword(encoder.encode(password));

        return userRepository.save(user);
    }

}