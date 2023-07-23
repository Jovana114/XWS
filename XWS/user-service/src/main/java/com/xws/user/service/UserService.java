package com.xws.user.service;

import com.xws.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(String userId);

    User updateUser(String userId, User updatedUser);

    void updateUserData(String userId, User updatedUser);

    void updateUserRole(String userId, String role);

    void updateUsername(String userId, String newUsername);

    boolean updatePassword(String userId, String oldPassword, String newPassword);

    void deleteUser(String userId);
}
