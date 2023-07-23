package com.xws.user.controller;

import com.xws.user.entity.User;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/data")
    public ResponseEntity<?> updateUserData(@PathVariable String userId, @RequestBody User updatedUser) {
        userService.updateUserData(userId, updatedUser);

        return ResponseEntity.ok(new MessageResponse("User data updated successfully!"));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable String userId, @RequestBody String role) {
        userService.updateUserRole(userId, role);

        return ResponseEntity.ok(new MessageResponse("User role updated successfully!"));
    }

    @PutMapping("/{userId}/username")
    public ResponseEntity<?> updateUsername(@PathVariable String userId, @RequestBody Map<String, String> request) {
        String newUsername = request.get("username");
        if (newUsername == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid request: 'username' attribute is missing."));
        }

        userService.updateUsername(userId, newUsername);

        return ResponseEntity.ok(new MessageResponse("Username updated successfully!"));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String userId, @RequestBody Map<String, String> request) {
        String oldPassword = request.get("old_password");
        String newPassword = request.get("new_password");

        if (newPassword == null || oldPassword == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid request: 'old_password' or 'new_password' attributes are missing."));
        }

        boolean isPasswordUpdated = userService.updatePassword(userId, oldPassword, newPassword);

        if (isPasswordUpdated) {
            return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Old password is incorrect."));
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}
