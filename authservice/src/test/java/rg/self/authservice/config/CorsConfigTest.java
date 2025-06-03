package rg.self.authservice.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;

class CorsConfigTest {

	@Test
	void testCorsFilterBeanExists() {
		CorsConfig config = new CorsConfig();
		CorsConfigurationSource source = config.corsConfigurationSource();
		assertNotNull(source);
	}
}
