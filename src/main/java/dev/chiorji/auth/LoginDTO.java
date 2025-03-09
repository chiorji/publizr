package dev.chiorji.auth;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;

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
