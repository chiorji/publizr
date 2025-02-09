package dev.publizr.user.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignUpDTO(
	@NotEmpty
	@NotNull
	String username,

	@NotEmpty
	@NotNull
	String email,

	@NotEmpty
	@NotNull
	String password
) {}
