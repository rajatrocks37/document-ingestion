package rg.self.documentmanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DocumentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void uploadShouldReturn200ForValidFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "This is content".getBytes());

		mockMvc.perform(multipart("/documents/upload").file(file).param("uploadedBy", "test_user"))
				.andExpect(status().isOk());
	}
}
