package com.xws.user.controller;

import com.xws.user.entity.ERole;
import com.xws.user.entity.Role;
import com.xws.user.payload.response.MessageResponse;
import com.xws.user.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class TestController {

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/guest")
	@PreAuthorize("hasRole('GUEST')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/host")
	@PreAuthorize("hasRole('HOST')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@PostMapping("/createHost")
	public ResponseEntity<?> createHost() {
		Role role = new Role(ERole.ROLE_HOST);
		roleRepository.save(role);
		return ResponseEntity.ok(new MessageResponse("Host role created successfully!"));
	}
	@PostMapping("/createGuest")
	public ResponseEntity<?> createGuest() {
		Role role = new Role(ERole.ROLE_GUEST);
		roleRepository.save(role);
		return ResponseEntity.ok(new MessageResponse("Guest role created successfully!"));
	}
}
