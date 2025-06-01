package rg.self.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rg.self.authservice.dto.GenericResponse;
import rg.self.authservice.dto.RegisterRequest;
import rg.self.authservice.service.AuthService;
import rg.self.authservice.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private AuthService authService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userDetailsService.getAllUsers());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
		try {
			authService.register(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse("User created successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericResponse("Failed to create user"));
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String username) {
		try {
			userDetailsService.deleteUser(username);
			return ResponseEntity.noContent().build();
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.badRequest().body("Failed to delete user: " + e.getMessage());
		}
	}
}
