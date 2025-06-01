package rg.self.documentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private String message;
	private String error;
	private Integer status;
	private String path;
	private LocalDateTime timestamp;

	// Constructor for simple message
	public ErrorResponse(String message) {
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	// Constructor with message and status
	public ErrorResponse(String message, Integer status) {
		this.message = message;
		this.status = status;
		this.timestamp = LocalDateTime.now();
	}

	// Constructor with all error details
	public ErrorResponse(String message, String error, Integer status, String path) {
		this.message = message;
		this.error = error;
		this.status = status;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}
}