package dev.publizr.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
	@NotEmpty
	@Size(min = 4, message = "username should be a minimum of 4 characters")
	String username,

	@NotEmpty
	@Email(message = "Email should be a valid email format")
	String email,

	@NotEmpty
	@Size(min = 8, message = "Password have a minimum length of 8 characters")
	String password
) {}
