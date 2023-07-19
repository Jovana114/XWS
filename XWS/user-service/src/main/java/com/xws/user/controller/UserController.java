package com.xws.user.controller;

import com.xws.user.entity.User;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        userService.updateUser(userId, updatedUser);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}
