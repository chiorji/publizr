package dev.publizr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
	@Value("${secret.key}")
	private String secretKey;

	@Value("${secret.token.issuer}")
	private String tokenIssuer;

	@Bean
	public FilterRegistrationBean<JWTFilter> filterRegistrationBean() {
		FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();

		JWTFilter jwtFilter = new JWTFilter(secretKey, tokenIssuer);
		registrationBean.setFilter(jwtFilter);

		registrationBean.addUrlPatterns("/api/posts/publish", "/api/users/list", "/api/posts/update", "/api/posts/delete");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}
}
