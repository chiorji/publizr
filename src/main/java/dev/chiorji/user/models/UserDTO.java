package dev.chiorji.user.models;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.annotation.*;
import jakarta.validation.constraints.*;
import java.util.*;
import jdk.jfr.*;
import org.springframework.data.annotation.*;
import org.springframework.format.annotation.*;

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
	Date updated_at,

	@BooleanFlag
	Boolean is_deleted
) {
}
