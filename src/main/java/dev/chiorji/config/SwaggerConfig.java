package dev.chiorji.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.*;
import java.util.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(info())
			.servers(List.of(new Server().url("http://localhost:8080").description("local")))
			.components(new Components().addSecuritySchemes("Bearer Auth", securityScheme()));
	}

	private Info info() {
		return new Info()
			.title("Publizr")
			.description("API developed as part of my SIWES project: JDBC, RESTFul API, Security, JWT, PostgresSQL and more")
			.version("0.0.1")
			.contact(contact());
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");
	}

	private Contact contact() {
		return new Contact()
			.name("Orji Chigbogu")
			.email("brightorji60@gmail.com")
			.url("https://github.com/chiorji");
	}
}
