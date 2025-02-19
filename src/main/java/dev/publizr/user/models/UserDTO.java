package dev.publizr.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Schema(description = "User Data Transfer Object")
public record UserDTO(
	@Id
	@NotNull
	@Schema(description = "Generated ID for the user")
	Integer id,

	@NotEmpty
	@NotNull
	@Schema(description = "Preferred username")
	String username,

	@NotEmpty
	@NotNull
	@Schema(description = "Users email address, must be valid", example = "johndoe@email.com")
	String email,

	@NotEmpty
	@Schema(description = "The role assigned the the user", example = "AUTHOR")
	String role,

	@Nullable
	@Schema(description = "This is the user's chosen profile picture")
	String image_url,

	@Schema(description = "The date and time user signed up on the platform")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date created_at,

	@Schema(description = "The recent date and time user updated his profile information on the platform")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date updated_at
) {
}
