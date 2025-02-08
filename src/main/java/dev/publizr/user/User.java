package dev.publizr.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record User(
	@NotEmpty
	@NotNull
	Integer id,

	@NotEmpty
	@NotNull
	String username,

	@NotEmpty
	@NotNull
	String email,

	@NotEmpty
	@NotNull
	String password,

	String role,

	String image_url,

	@NotNull
	LocalDateTime created_at,

	@NotNull
	LocalDateTime updated_at

) {
}
