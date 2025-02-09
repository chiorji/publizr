package dev.publizr.user.models;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
	@NotEmpty
	String email,

	@NotEmpty
	String password
) {
}
