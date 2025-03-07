package dev.chiorji.user.models;

import jakarta.annotation.*;
import jakarta.validation.constraints.*;
import java.util.*;
import jdk.jfr.*;
import org.springframework.data.annotation.*;
import org.springframework.format.annotation.*;

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
	Date updated_at,

	@BooleanFlag
	Boolean is_deleted
) {}
