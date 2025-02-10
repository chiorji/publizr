package dev.publizr.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				      .title("Publizr")
				      .description("API developed as part of my SIWES project: JDBC, RESTFul API, Security, JWT, PostgresSQL and more")
				      .version("0.0.1")
				      .contact(new Contact()
					               .name("Orji Chigbogu")
					               .email("brightorji60@gmail.com")
					               .url("https://github.com/chiorji"))
			).servers(List.of(new Server().url("http://localhost:8080").description("local")));
	}
}
