package dev.chiorji.config;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebMvcConfig implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

	@Value("${secret.key}")
	private String secretKey;

	@Value("${secret.token.issuer}")
	private String tokenIssuer;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("WebMvcConfig: Configuring CORS");
		registry.addMapping("/api/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("*");
	}
}
