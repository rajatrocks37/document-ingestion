package rg.self.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.LoginRequest;
import rg.self.authservice.dto.UserRegistrationRequest;
import rg.self.authservice.entity.User;
import rg.self.authservice.entity.User.Role;
import rg.self.authservice.repository.UserRepository;
import rg.self.authservice.security.JwtService;

@SpringBootTest
class AuthServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private AuthenticationManager authenticationManager;

	private AuthService authService;

	@BeforeEach
	void setUp() {
		authService = new AuthService(userRepository, jwtService, authenticationManager);
	}

	@Test
	void testRegisterSuccess() {
		UserRegistrationRequest request = new UserRegistrationRequest("user1", "password");
		when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User response = authService.register(request);

		assertEquals("user1", response.getUsername());
		verify(userRepository).save(any(User.class));
	}

	@Test
	void testLoginSuccess() {
		LoginRequest request = new LoginRequest("user1", "password");
		Authentication auth = mock(Authentication.class);

		// Create mock user
		User mockUser = new User();
		mockUser.setUsername("user1");
		mockUser.setRole(Role.ADMIN);

		when(auth.getPrincipal()).thenReturn(mockUser);
		when(authenticationManager.authenticate(any())).thenReturn(auth);
		when(jwtService.generateToken(request.getUsername(), Role.ADMIN)).thenReturn("token123");

		AuthResponse response = authService.login(request);

		assertEquals("token123", response.getToken());
		assertEquals("user1", response.getUsername());
		assertEquals(Role.ADMIN, response.getRole());
	}

	@Test
	void testLoginFailure() {
		LoginRequest request = new LoginRequest("user1", "wrongpassword");
		when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

		assertThrows(RuntimeException.class, () -> authService.login(request));
	}
}
