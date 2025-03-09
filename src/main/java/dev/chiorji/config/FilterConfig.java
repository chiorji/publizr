package dev.chiorji.config;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;
import org.springframework.core.*;

@Configuration
public class FilterConfig {
	private static final Logger log = LoggerFactory.getLogger(FilterConfig.class);
	@Value("${secret.key}")
	private String secretKey;

	@Value("${secret.token.issuer}")
	private String tokenIssuer;

	public FilterConfig() {
		log.info("FilterConfig: Initialized");
	}

	@Bean
	public FilterRegistrationBean<CustomJWTFilter> filterRegistrationBean() {
		System.out.println("FilterConfig: Registering CustomJWTFilter");
		FilterRegistrationBean<CustomJWTFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomJWTFilter(secretKey, tokenIssuer));
		registrationBean.addUrlPatterns("/api/posts/delete/*", "/api/posts/publish", "/api/posts/feature/*", "/api/posts/update/*", "/api/users/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return registrationBean;
	}
}
