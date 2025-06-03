package rg.self.authservice.dto;

public class UserRegistrationResponse extends GenericResponse {

	private String username;

	public UserRegistrationResponse(String username, String message) {
		super(message);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
