package dev.chiorji.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for login")
public record LoginDTO(
	@NotEmpty
	@Email
	@Schema(description = "Users email address, must be valid", example = "chigbo@orji.com")
	String email,

	@NotEmpty
	@Size(min = 6, message = "Password should have a minimum of 6 characters")
	@Schema(description = "Password provided while signing up", example = "12@Password")
	String password
) {}
