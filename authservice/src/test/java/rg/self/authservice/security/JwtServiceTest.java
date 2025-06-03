package rg.self.authservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import rg.self.authservice.entity.User;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	private JwtService jwtService;

	@Mock
	private ResourceLoader resourceLoader;

	@BeforeEach
	void setUp() throws Exception {
		jwtService = new JwtService(resourceLoader);

		// Manually inject a test signing key
		Field field = JwtService.class.getDeclaredField("privateKey");
		field.setAccessible(true);

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keyPair = keyGen.generateKeyPair();

		field.set(jwtService, keyPair.getPrivate());
	}

	@Test
	void testGenerateAndValidateToken() {
		User user = new User();
		user.setUsername("user");
		user.setPassword("pass");
		user.setEnabled(true);
		user.setRole(User.Role.EDITOR);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				List.of(new SimpleGrantedAuthority("EDITOR")));

		String token = jwtService.generateToken(auth.getName(), User.Role.EDITOR);
		assertNotNull(token);

		String username = jwtService.extractUsername(token);
		assertEquals("user", username);
	}
}
