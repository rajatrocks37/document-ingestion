package rg.self.documentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import rg.self.documentmanagement.dto.DocumentSnippetDTO;
import rg.self.documentmanagement.service.QnAService;

@RestController
@RequestMapping("/api/qna")
public class QnAController {

	@Autowired
	private QnAService service;

	@GetMapping
	@Operation(summary = "find documents using keywords")
	public ResponseEntity<List<DocumentSnippetDTO>> ask(@RequestParam String question) {
		return ResponseEntity.ok(service.answerQuestion(question));
	}
}
