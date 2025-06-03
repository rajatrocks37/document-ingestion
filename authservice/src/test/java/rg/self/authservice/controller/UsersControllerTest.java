package rg.self.authservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import rg.self.authservice.config.TestSecurityConfig;
import rg.self.authservice.dto.UserCreationRequest;
import rg.self.authservice.entity.User;
import rg.self.authservice.repository.UserRepository;
import rg.self.authservice.security.JwtService;
import rg.self.authservice.service.AuthService;
import rg.self.authservice.service.CustomUserDetailsService;

@WebMvcTest(UsersController.class)
@Import(TestSecurityConfig.class)
class UsersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private CustomUserDetailsService userDetailsService;

	@MockBean
	private AuthService authService;

	@MockBean
	private JwtService jwtService;

	@Autowired
	private ObjectMapper objectMapper;

	private UserCreationRequest sampleUser;

	@BeforeEach
	void setUp() {
		sampleUser = new UserCreationRequest("newuser", "password", "ADMIN");
	}

//	@Test
//	@WithMockUser(roles = "ADMIN")
//	void testGetUsers() throws Exception {
//		List<User> users = List.of(new User("user1", "pass1", Role.EDITOR), new User("user2", "pass2", Role.ADMIN));
//		when(userDetailsService.getAllUsers()).thenReturn(users);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/users")).andExpect(status().isOk())
//				.andExpect(jsonPath("$.length()").value(2));
//	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateUserSuccess() throws Exception {

		when(authService.createUser(any())).thenReturn(new User());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleUser))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("User created successfully"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateUserFailure() throws Exception {
		doThrow(new RuntimeException("DB error")).when(authService).createUser(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(sampleUser))).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message").value("Failed to create user"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testDeleteUserSuccess() throws Exception {
		doNothing().when(userDetailsService).deleteUser("testuser");

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/testuser")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testDeleteUserFailure() throws Exception {
		doThrow(new UsernameNotFoundException("Not found")).when(userDetailsService).deleteUser("baduser");

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/baduser")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Failed to delete user: Not found"));
	}
}