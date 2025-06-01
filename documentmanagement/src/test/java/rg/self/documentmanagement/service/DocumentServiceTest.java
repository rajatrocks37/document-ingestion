package rg.self.documentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import rg.self.documentmanagement.repository.DocumentRepository;

@SpringBootTest
class DocumentServiceTest {

	@Autowired
	private DocumentService documentService;

	@MockBean
	private DocumentRepository documentRepository;

	@Test
	void shouldThrowExceptionForUnsupportedFileType() {
		MockMultipartFile file = new MockMultipartFile("file", "test.exe", "application/exe", new byte[10]);
		assertThrows(IllegalArgumentException.class, () -> documentService.ingestDocumentAsync(file, "user"));
	}

	@Test
	void shouldThrowExceptionForLargeFile() {
		byte[] largeBytes = new byte[11 * 1024 * 1024]; // 11MB
		MockMultipartFile file = new MockMultipartFile("file", "large.pdf", "application/pdf", largeBytes);
		assertThrows(IllegalArgumentException.class, () -> documentService.ingestDocumentAsync(file, "user"));
	}
}
