package dev.chiorji.user.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date created_at,

	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date updated_at

) {}
