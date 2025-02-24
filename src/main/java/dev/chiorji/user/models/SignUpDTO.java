package dev.chiorji.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for signup")
public record SignUpDTO(
	@NotEmpty
	@Size(min = 4, message = "username should be a minimum of 4 characters")
	@Schema(description = "This is the author's display name", example = "Chigbo")
	String username,

	@NotEmpty
	@Email(message = "Email should be a valid email format", regexp = "^(.+)@(.+)$", flags = Pattern.Flag.CASE_INSENSITIVE)
	@Schema(description = "Users email address, must be valid", example = "chigbo@orji.com")
	String email,

	@NotEmpty
	@Size(min = 6, message = "Password should have a minimum of 6 characters")
	@Schema(description = "Password can be a combination of alphanumeric characters", example = "12@Password")
	String password
) {}
