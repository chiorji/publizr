package dev.publizr.user.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
	@NotEmpty
	@NotNull
	String email,

	@NotEmpty
	@NotNull
	String password
) {
}
