package rg.self.documentmanagement.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import rg.self.documentmanagement.dto.DocumentResponseDto;
import rg.self.documentmanagement.entity.Document;
import rg.self.documentmanagement.repository.DocumentRepository;

@Service
public class DocumentService {

	private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private DocumentRepository documentRepository;

	private final Tika tika = new Tika();

	public List<Document> getAllDocuments() {
		return documentRepository.findAll();
	}

	public Page<Document> search(Optional<String> title, Optional<String> type, Optional<String> uploadedBy,
			Pageable pageable) {
		return documentRepository.findByFilters(title.orElse(""), type.orElse(""), uploadedBy.orElse(""), pageable);
	}

	@Async
	public CompletableFuture<DocumentResponseDto> ingestDocumentAsync(MultipartFile file, String uploadedBy)
			throws IOException, TikaException {

		validateFile(file);

		String fileName = file.getOriginalFilename();
		String contentType = file.getContentType();
		String content = tika.parseToString(file.getInputStream());

		// Extract metadata
		Metadata metadata = new Metadata();
		tika.parse(file.getInputStream(), metadata);

		String author = metadata.get("Author");
		List<String> keywords = parseKeywords(metadata.get("Keywords"));

		Document document = new Document();
		document.setTitle(fileName);
		document.setType(contentType);
		document.setContent(content);
		document.setUploadedBy(uploadedBy);
		document.setUploadedAt(LocalDateTime.now());
		document.setAuthor(author);
		document.setKeywords(keywords);
		document.setSummary(generateSummary(content));

		Document saved = documentRepository.save(document);

		return CompletableFuture.completedFuture(toDto(saved));
	}

	public CompletableFuture<Boolean> deleteDocumentAsync(Long id, String deletedBy) {
		return CompletableFuture.supplyAsync(() -> {
			Optional<Document> documentOpt = documentRepository.findById(id);

			if (documentOpt.isPresent()) {
				log.info("Document {} deleted by user {}", id, deletedBy);
				documentRepository.deleteById(id);
				return true;
			}
			return false;
		});
	}

	private void validateFile(MultipartFile file) {
		String type = file.getContentType();
		if (!(type.equals("application/pdf")
				|| type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
				|| type.equals("text/plain"))) {
			throw new IllegalArgumentException("Unsupported file type");
		}

		if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
			throw new IllegalArgumentException("File too large");
		}
	}

	private List<String> parseKeywords(String keywordStr) {
		if (keywordStr == null)
			return List.of();
		return Arrays.stream(keywordStr.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
	}

	private String generateSummary(String content) {
		String[] sentences = content.split("\\. ");
		return sentences.length > 2 ? sentences[0] + ". " + sentences[1] + "." : content;
	}

	private DocumentResponseDto toDto(Document doc) {
		DocumentResponseDto dto = new DocumentResponseDto();
		dto.setId(doc.getId());
		dto.setTitle(doc.getTitle());
		dto.setType(doc.getType());
		dto.setUploadedBy(doc.getUploadedBy());
		dto.setUploadedAt(doc.getUploadedAt());
		dto.setAuthor(doc.getAuthor());
		dto.setKeywords(doc.getKeywords());
		dto.setSummary(doc.getSummary());
		return dto;
	}

}
