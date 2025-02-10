package dev.publizr.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record User(
	@Id
	@NotNull
	@Schema(description = "Generated ID for the user")
	Integer id,

	@NotEmpty
	@Schema(description = "Preferred username")
	String username,

	@NotEmpty
	@Schema(description = "Users email address, must be valid", example = "johndoe@email.com")
	String email,

	@NotEmpty
	@Size(min = 6, message = "Password should have a minimum of 6 characters")
	@Schema(description = "User's password a combination of alphanumeric characters", example = "12@Password")
	String password,

	@NotEmpty
	@Schema(description = "The role assigned the the user", example = "AUTHOR")
	String role,

	@Nullable
	@Schema(description = "This is the user's chosen profile picture")
	String image_url,

	@NotNull
	@PastOrPresent
	@Schema(description = "The date and time user signed up on the platform")

	LocalDateTime created_at,

	@NotNull
	@PastOrPresent
	@Schema(description = "The recent date and time user updated his profile information on the platform")
	LocalDateTime updated_at

) {}
