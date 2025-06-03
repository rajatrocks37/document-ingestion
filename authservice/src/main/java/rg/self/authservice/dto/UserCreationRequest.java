package rg.self.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
	private String username;
	private String password;
	private String role;

	public UserCreationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}