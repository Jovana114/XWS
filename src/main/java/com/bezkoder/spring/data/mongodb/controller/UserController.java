package com.bezkoder.spring.data.mongodb.controller;

import java.util.*;

import com.bezkoder.spring.data.mongodb.model.ERole;
import com.bezkoder.spring.data.mongodb.model.Role;
import com.bezkoder.spring.data.mongodb.payload.request.SignupRequest;
import com.bezkoder.spring.data.mongodb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.data.mongodb.model.user;
import com.bezkoder.spring.data.mongodb.repository.UserRepository;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @GetMapping("/get_users")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<List<user>> getAllUsers(@RequestParam(required = false) String name) {
    try {
      List<user> users = new ArrayList<user>();

      if (name == null)
        userRepository.findAll().forEach(users::add);
      else
        userRepository.findByNameContaining(name).forEach(users::add);

      if (users.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/get_users/{id}")
  public ResponseEntity<user> getUserById(@PathVariable("id") Long id) {
    Optional<user> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      return new ResponseEntity<>(userData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/create_users")
  public ResponseEntity<user> createUser(@RequestBody user user_new) {
    try {
      user _user = userRepository.save(new user(user_new.getName(), user_new.getUsername(), user_new.getEmail(),
             user_new.getPassword()));

      userRepository.save(_user);
      return new ResponseEntity<>(_user, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/update_users/{id}")
  public ResponseEntity<user> updateUser(@PathVariable("id") Long id, @RequestBody user user_new) {
    Optional<user> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      user _user = userData.get();
      _user.setName(user_new.getName());
      _user.setEmail(user_new.getEmail());
      _user.setPassword(user_new.isPassword());
      return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/delete_users/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
    try {
      userRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/delete_users")
  public ResponseEntity<HttpStatus> deleteAllUsers() {
    try {
      userRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
