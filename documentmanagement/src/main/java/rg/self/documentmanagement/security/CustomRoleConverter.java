package rg.self.documentmanagement.security;

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		String role = jwt.getClaimAsString("role");
		if (role == null) {
			return List.of();
		}

		String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
		return List.of(new SimpleGrantedAuthority(prefixedRole));
	}
}
