package rg.self.authservice.security;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import rg.self.authservice.entity.User.Role;

@Service
public class JwtService {

	@Value("${jwt.private.key.path}")
	private String privateKeyPath;

	private final ResourceLoader resourceLoader;

	private PrivateKey privateKey;

	public JwtService(ResourceLoader resourceLoader) throws Exception {
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void init() throws Exception {
		Resource resource = resourceLoader.getResource(privateKeyPath);

		try (InputStream is = resource.getInputStream()) {
			if (is == null) {
				throw new FileNotFoundException("Private key not found in classpath");
			}
			String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
					.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
					.replaceAll("\\s", "");
			byte[] decoded = Base64.getDecoder().decode(key);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
			this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
		}
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public String generateToken(String username, Role role) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.claim("role", role).signWith(SignatureAlgorithm.RS256, privateKey).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername());
	}
}
