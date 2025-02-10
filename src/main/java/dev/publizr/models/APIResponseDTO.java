package dev.publizr.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;

@Schema(description = "Standard API Response Data Transfer Object")
public record APIResponseDTO<T>(
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
