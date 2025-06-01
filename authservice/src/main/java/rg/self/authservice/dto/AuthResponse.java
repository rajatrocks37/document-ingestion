package rg.self.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rg.self.authservice.entity.Users.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String username;
	private Role role;
	private String token;
}