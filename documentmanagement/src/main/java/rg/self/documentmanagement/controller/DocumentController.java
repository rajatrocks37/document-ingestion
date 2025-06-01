package rg.self.documentmanagement.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import rg.self.documentmanagement.dto.DocumentResponseDto;
import rg.self.documentmanagement.dto.ErrorResponse;
import rg.self.documentmanagement.entity.Document;
import rg.self.documentmanagement.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentService;

//	@GetMapping
//	public List<Document> getAllDocuments() {
//		return documentService.getAllDocuments();
//	}

	@GetMapping
	@Operation(summary = "Search uploaded documents")
	public Page<Document> searchDocuments(@RequestParam Optional<String> title, @RequestParam Optional<String> type,
			@RequestParam Optional<String> uploadedBy,
			@PageableDefault(size = 10, sort = "uploadedAt", direction = Direction.DESC) Pageable pageable) {

		return documentService.search(title, type, uploadedBy, pageable);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload a document file (PDF, DOCX, TXT)")
	public CompletableFuture<ResponseEntity<DocumentResponseDto>> uploadDocument(
			@RequestParam("file") MultipartFile file, Principal principal) throws IOException, TikaException {
		String uploadedBy = principal.getName();
		return documentService.ingestDocumentAsync(file, uploadedBy).thenApply(ResponseEntity::ok);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a document by ID")
	public CompletableFuture<ResponseEntity<Object>> deleteDocument(@PathVariable("id") Long id, Principal principal) {

		String deletedBy = principal.getName();

		return documentService.deleteDocumentAsync(id, deletedBy).thenApply(deleted -> {
			if (deleted) {
				return ResponseEntity.noContent().build(); // 204 No Content
			} else {
				return ResponseEntity.notFound().build(); // 404 Not Found
			}
		}).exceptionally(ex -> {
			log.error("Error deleting document with id: " + id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Failed to delete document: " + ex.getMessage()));
		});
	}
}
