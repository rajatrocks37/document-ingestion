package rg.self.authservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.LoginRequest;
import rg.self.authservice.dto.UserCreationRequest;
import rg.self.authservice.dto.UserRegistrationRequest;
import rg.self.authservice.entity.User;
import rg.self.authservice.entity.User.Role;
import rg.self.authservice.repository.UserRepository;
import rg.self.authservice.security.JwtService;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public AuthService(UserRepository userRepository, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public User register(UserRegistrationRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole(Role.VIEWER); // Default Role
		user.setEnabled(true);
		return userRepository.save(user);
	}

	public User createUser(UserCreationRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole(Role.valueOf(request.getRole()));
		user.setEnabled(true);
		return userRepository.save(user);
	}

	public AuthResponse login(LoginRequest req) {
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
		User user = (User) auth.getPrincipal();
		String token = jwtService.generateToken(req.getUsername(), user.getRole());

		AuthResponse response = new AuthResponse(user.getUsername(), user.getRole(), token);
		return response;
	}
}
