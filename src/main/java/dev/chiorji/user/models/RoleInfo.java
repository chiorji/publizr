package dev.chiorji.user.models;

import jakarta.validation.constraints.*;

public record RoleInfo(
	@NotEmpty
	String email,
	@NotEmpty
	String role
) {
}
