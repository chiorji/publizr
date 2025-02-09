package dev.publizr.user.models;

import jakarta.validation.constraints.NotEmpty;

public record SignUpDTO(
	@NotEmpty
	String username,

	@NotEmpty
	String email,

	@NotEmpty
	String password
) {}
