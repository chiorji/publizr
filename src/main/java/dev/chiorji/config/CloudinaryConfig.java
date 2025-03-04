package dev.chiorji.config;

import com.cloudinary.*;
import com.cloudinary.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.*;

@Configuration
public class CloudinaryConfig {
	@Value("${cloudinary.cloud.name}")
	private String cloudinaryCloudName;

	@Value("${cloudinary.cloud.api.key}")
	private String cloudinaryAPIKey;

	@Value("${cloudinary.cloud.api.secret}")
	private String cloudinaryAPISecret;

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
			"cloud_name", cloudinaryCloudName,
			"api_key", cloudinaryAPIKey,
			"api_secret", cloudinaryAPISecret
		));
	}
}
