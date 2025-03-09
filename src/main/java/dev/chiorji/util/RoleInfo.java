package dev.chiorji.util;

import jakarta.validation.constraints.*;

public record RoleInfo(
	@NotEmpty
	String email,
	@NotEmpty
	String role,
	@NotNull
	Integer id
) {
}
