package dev.chiorji.models;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.annotation.*;
import jakarta.validation.constraints.*;
import jdk.jfr.*;

@Schema(description = "Standard API Response Data Transfer Object")
public record ResponseDTO<T>(
	@NotNull
	@BooleanFlag
	@Schema(description = "Indicates if a request is successful", example = "true")
	Boolean success,

	@NotEmpty
	@Schema(description = "Response message", example = "Publication successful")
	String message,

	@Nullable
	@Schema(description = "A generic response data")
	T data,

	@Nullable
	@Schema(description = "Size of the response data")
	Integer size
) {
}
