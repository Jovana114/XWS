package com.xws.user.service.impl;

import com.xws.user.entity.ERole;
import com.xws.user.entity.Role;
import com.xws.user.entity.User;
import com.xws.user.repo.RoleRepository;
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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void updateUserData(String userId, User updatedUser) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Update the user data fields
            user.setFirst_name(updatedUser.getFirst_name());
            user.setLast_name(updatedUser.getLast_name());
            user.setAddress(updatedUser.getAddress());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(encoder.encode(updatedUser.getPassword()));

            userRepository.save(user);
        }
    }

    @Override
    public void updateUserRole(String userId, String role) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Find the role by name
            Role userRole = roleRepository.findByName(ERole.valueOf(role))
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            // Set the user's role
            user.getRoles().clear();
            user.getRoles().add(userRole);

            userRepository.save(user);
        }
    }

    @Override
    public void updateUsername(String userId, String newUsername) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Update the username
            user.setUsername(newUsername);
            userRepository.save(user);
        }
    }

    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();

        // Check if the old password is correct
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return false; // Old password is incorrect
        }

        // Update the password
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return true; // Password updated successfully
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
        user.setPassword(encoder.encode(updatedUser.getPassword()));
        user.setAddress(updatedUser.getAddress());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found!");
        }

        userRepository.deleteById(userId);
    }
}