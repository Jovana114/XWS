package xws.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xws.model.ERole;
import xws.model.Role;
import xws.payload.response.MessageResponse;
import xws.repository.RoleRepository;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@PostMapping("/createAdmin")
	public ResponseEntity<?> createAdmin() {
		Role role = new Role(ERole.ROLE_ADMIN);
		roleRepository.save(role);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	@PostMapping("/createMod")
	public ResponseEntity<?> createMod() {
		Role role = new Role(ERole.ROLE_MODERATOR);
		roleRepository.save(role);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser() {
		Role role = new Role(ERole.ROLE_USER);
		roleRepository.save(role);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
