package com.xws.user.controller;

import com.xws.user.entity.User;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.repo.UserRepository;
import com.xws.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUserById/{user_id}")
    public User getUserById(@PathVariable("user_id") String user_id){
        Optional<User> user = userRepository.findById(user_id);
        return user.get();
    }

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
