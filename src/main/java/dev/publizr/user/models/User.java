package dev.publizr.user.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record User(
	@Id
	@NotNull
	Integer id,

	@NotEmpty
	String username,

	@NotEmpty
	String email,

	@NotEmpty
	String password,

	@NotEmpty
	String role,

	@Nullable
	String image_url,

	@NotNull
	LocalDateTime created_at,

	@NotNull
	LocalDateTime updated_at

) {
}
