package rg.self.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.LoginRequest;
import rg.self.authservice.dto.UserRegistrationRequest;
import rg.self.authservice.dto.UserRegistrationResponse;
import rg.self.authservice.entity.User;
import rg.self.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
		User user = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new UserRegistrationResponse(user.getUsername(), "User registered successfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication auth) {
		return ResponseEntity.ok(auth.getPrincipal());
	}
}