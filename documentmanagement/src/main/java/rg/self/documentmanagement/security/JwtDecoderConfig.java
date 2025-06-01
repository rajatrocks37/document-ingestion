package rg.self.documentmanagement.security;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import jakarta.annotation.PostConstruct;

@Configuration
public class JwtDecoderConfig {

	@Value("${jwt.public.key.path}")
	private String publicKeyPath;

	private final ResourceLoader resourceLoader;
	private PublicKey publicKey = null;

	public JwtDecoderConfig(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void init() throws Exception {
		Resource resource = resourceLoader.getResource(publicKeyPath);

		try (InputStream is = resource.getInputStream()) {
			if (is == null) {
				throw new FileNotFoundException("Public key not found in classpath");
			}
			String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
					.replaceAll("\\s", "");
			byte[] keyBytes = Base64.getDecoder().decode(key);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			this.publicKey = kf.generatePublic(spec);
		}
	}

	@Bean
	public JwtDecoder jwtDecoder() throws Exception {
		RSAPublicKey publicKey = (RSAPublicKey) loadPublicKey();
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}

	private PublicKey loadPublicKey() {
		return this.publicKey;
	}
}