package dev.chiorji.category;

import jakarta.validation.constraints.*;

public record Category(
	@NotNull
	Integer id,
	@NotNull
	String name
) {
}
