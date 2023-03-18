package xws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xws.model.User;
import xws.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  UserRepository userRepository;

//  @GetMapping("/get_users")
//  public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String name) {
//    try {
//      List<User> Users = new ArrayList<User>();
//
//      if (name == null)
//        userRepository.findAll().forEach(Users::add);
//      else
//        userRepository.findByNameContaining(name).forEach(Users::add);
//
//      if (Users.isEmpty()) {
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//      }
//
//      return new ResponseEntity<>(Users, HttpStatus.OK);
//    } catch (Exception e) {
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @GetMapping("/get_users/{id}")
//  public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
//    Optional<User> userData = userRepository.findById(id);
//
//    if (userData.isPresent()) {
//      return new ResponseEntity<>(userData.get(), HttpStatus.OK);
//    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @PostMapping("/create_users")
//  public ResponseEntity<User> createUser(@RequestBody User user_new) {
//    try {
//      User _user = userRepository.save(new User(user_new.getName(), user_new.getEmail(), user_new.isPassword()));
//      return new ResponseEntity<>(_user, HttpStatus.CREATED);
//    } catch (Exception e) {
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @PutMapping("/update_users/{id}")
//  public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user_new) {
//    Optional<User> userData = userRepository.findById(id);
//
//    if (userData.isPresent()) {
//      User _user = userData.get();
//      _user.setName(user_new.getName());
//      _user.setEmail(user_new.getEmail());
//      _user.setPassword(user_new.isPassword());
//      return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
//    } else {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @DeleteMapping("/delete_users/{id}")
//  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
//    try {
//      userRepository.deleteById(id);
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @DeleteMapping("/delete_tutorials")
//  public ResponseEntity<HttpStatus> deleteAllUsers() {
//    try {
//      userRepository.deleteAll();
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

}
