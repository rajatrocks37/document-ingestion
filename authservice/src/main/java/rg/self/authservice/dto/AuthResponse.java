package rg.self.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rg.self.authservice.entity.User.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String username;
	private Role role;
	private String token;
}