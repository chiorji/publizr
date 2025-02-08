package dev.publizr.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginPayload(
	@NotEmpty
	@NotNull
	String email,

	@NotEmpty
	@NotNull
	String password
) {
}
