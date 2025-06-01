package rg.self.documentmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Document Ingestion & Q&A API").version("1.0.0")
						.description("This is the backend API for document upload, ingestion, and question-answering.")
						.contact(new Contact().name("Support Team").email("support@rg.com"))
						.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
						new SecurityScheme().name(SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")));
	}
}