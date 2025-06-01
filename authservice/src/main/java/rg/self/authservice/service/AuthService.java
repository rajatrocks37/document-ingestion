package rg.self.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.RegisterRequest;
import rg.self.authservice.entity.Users;
import rg.self.authservice.entity.Users.Role;
import rg.self.authservice.repository.UserRepository;
import rg.self.authservice.security.JwtService;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public Users register(RegisterRequest request) {
		Users user = new Users();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		Role role = StringUtils.hasLength(request.getRole()) ? Role.valueOf(request.getRole()) : Role.VIEWER;
		user.setRole(role); // Default Role
		user.setEnabled(true);
		return userRepository.save(user);
	}

	public AuthResponse login(String username, String password, AuthenticationManager authManager) {
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		Users user = (Users) auth.getPrincipal();
		String token = jwtService.generateToken(username, user.getRole());

		AuthResponse response = new AuthResponse(user.getUsername(), user.getRole(), token);
		return response;
	}
}
