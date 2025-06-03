package rg.self.authservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import rg.self.authservice.config.TestSecurityConfig;
import rg.self.authservice.dto.AuthResponse;
import rg.self.authservice.dto.LoginRequest;
import rg.self.authservice.dto.UserRegistrationRequest;
import rg.self.authservice.entity.User;
import rg.self.authservice.entity.User.Role;
import rg.self.authservice.repository.UserRepository;
import rg.self.authservice.security.JwtService;
import rg.self.authservice.service.AuthService;
import rg.self.authservice.service.CustomUserDetailsService;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testRegister() throws Exception {
		UserRegistrationRequest request = new UserRegistrationRequest("user", "pass");
		User user = new User();
		user.setUsername("user");
		when(authService.register(request)).thenReturn(user);

		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("User registered successfully"))
				.andExpect(jsonPath("$.username").value("user"));
	}

	@Test
	void testLogin() throws Exception {
		LoginRequest request = new LoginRequest("user", "pass");

		when(authService.login(request)).thenReturn(new AuthResponse("user", Role.ADMIN, "token123"));

		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.token").value("token123")).andExpect(jsonPath("$.username").value("user"));
	}
}
