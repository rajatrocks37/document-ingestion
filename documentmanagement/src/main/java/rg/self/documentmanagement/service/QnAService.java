package rg.self.documentmanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import rg.self.documentmanagement.dto.DocumentSnippetDTO;
import rg.self.documentmanagement.entity.Document;
import rg.self.documentmanagement.repository.DocumentRepository;

@Service
public class QnAService {

	private final DocumentRepository repository;

	public QnAService(DocumentRepository repository) {
		this.repository = repository;
	}

	public List<DocumentSnippetDTO> answerQuestion(String question) {
		List<Document> docs = repository.searchByKeyword(question);
		return docs.stream()
				.map(doc -> new DocumentSnippetDTO(doc.getTitle(), extractSnippet(doc.getContent(), question)))
				.collect(Collectors.toList());
	}

	private String extractSnippet(String content, String keyword) {
		int index = content.toLowerCase().indexOf(keyword.toLowerCase());
		if (index == -1)
			return content.substring(0, Math.min(200, content.length()));

		int start = Math.max(0, index - 50);
		int end = Math.min(content.length(), index + 150);
		return content.substring(start, end) + "...";
	}
}
