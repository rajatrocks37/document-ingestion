package rg.self.documentmanagement.dto;

import java.util.Optional;

public class DocumentSearchRequest {

	Optional<String> title;
	Optional<String> type;
	Optional<String> uploadedBy;
}
