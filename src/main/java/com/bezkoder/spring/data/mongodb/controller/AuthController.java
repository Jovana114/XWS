package com.bezkoder.spring.data.mongodb.controller;

import java.util.*;

import com.bezkoder.spring.data.mongodb.model.ERole;
import com.bezkoder.spring.data.mongodb.model.Role;
import com.bezkoder.spring.data.mongodb.model.user;
import com.bezkoder.spring.data.mongodb.payload.request.LoginRequest;
import com.bezkoder.spring.data.mongodb.payload.request.SignupRequest;
import com.bezkoder.spring.data.mongodb.payload.response.JwtResponse;
import com.bezkoder.spring.data.mongodb.repository.RoleRepository;
import com.bezkoder.spring.data.mongodb.repository.UserRepository;
import com.bezkoder.spring.data.mongodb.security.jwt.JwtUtils;
import com.bezkoder.spring.data.mongodb.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    Optional<user> u = userRepository.findById(userDetails.getId());
    if(u.isPresent()) {
      user _u = u.get();
      return ResponseEntity.ok(new JwtResponse(
              jwt, _u.getId(), _u.getRoles()));
    } else  {
      return ResponseEntity.badRequest().body("Error");
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Username is already taken!");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Email is already in use!");
    }

    user u = new user(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),
            signUpRequest.getName());
    String strRole = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    System.out.println(roles);

    Set<String> strRoles = new HashSet<String>(Arrays.asList(strRole.split(", ")));

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "ROLE_ADMIN":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "ROLE_MODERATOR":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    u.setRoles(roles);
    userRepository.save(u);

    return ResponseEntity.ok("User registered successfully!");
  }

}
