package rg.self.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import rg.self.authservice.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(
						authz -> authz.requestMatchers("/api/**").permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder()).and().build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    @Bean
//    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        return http.formLogin().and().build();
//    }
//
//    @Bean
//    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("document-client")
//                .clientSecret(passwordEncoder.encode("secret"))
//                .scope("read")
//                .scope("write")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenTimeToLive(Duration.ofHours(1))
//                        .refreshTokenTimeToLive(Duration.ofDays(1))
//                        .build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(registeredClient);
//    }
//
//    @Bean
//    public AuthorizationServerSettings providerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
