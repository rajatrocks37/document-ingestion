package rg.self.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.GenericResponse;
import rg.self.authservice.dto.LoginRequest;
import rg.self.authservice.dto.RegisterRequest;
import rg.self.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private AuthenticationManager authManager;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		request.setRole(null);
		authService.register(request);
		return ResponseEntity.ok(new GenericResponse("User registered successfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request.getUsername(), request.getPassword(), authManager);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication auth) {
		return ResponseEntity.ok(auth.getPrincipal());
	}
}