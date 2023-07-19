package com.xws.user.service;

import com.xws.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(String userId);

    User updateUser(String userId, User updatedUser);

    void deleteUser(String userId);
}
