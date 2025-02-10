package dev.publizr.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Schema(description = "User Data Transfer Object")
public record UserDTO(
	@Id
	@NotNull
	Integer id,

	@NotEmpty
	@NotNull
	String username,

	@NotEmpty
	@NotNull
	String email,

	@NotEmpty
	String role,

	@Nullable
	String image_url,

	@PastOrPresent
	LocalDateTime created_at,

	@PastOrPresent
	LocalDateTime updated_at
) {
}
