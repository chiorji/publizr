package dev.chiorji.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;

@Configuration
public class FilterConfig {
	@Value("${secret.key}")
	private String secretKey;

	@Value("${secret.token.issuer}")
	private String tokenIssuer;

	@Bean
	public FilterRegistrationBean<CustomJWTFilter> filterRegistrationBean() {
		FilterRegistrationBean<CustomJWTFilter> registrationBean = new FilterRegistrationBean<>();
		CustomJWTFilter customJwtFilter = new CustomJWTFilter(secretKey, tokenIssuer);
		registrationBean.setFilter(customJwtFilter);
		registrationBean.addUrlPatterns("/posts");
		registrationBean.addUrlPatterns("/users");
		registrationBean.setOrder(2);
		return registrationBean;
	}
}
