package dev.publizr;

import dev.publizr.jwt.JWTFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAutoConfiguration()
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

	@Bean
	public FilterRegistrationBean<JWTFilter> filterFilterRegistrationBean() {
		FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();
		JWTFilter jwtFilter = new JWTFilter();
		registrationBean.setFilter(jwtFilter);
		registrationBean.addUrlPatterns("/api/posts/new");
		registrationBean.addUrlPatterns("/api/users/list");
		return registrationBean;
	}
}
