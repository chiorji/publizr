package dev.chiorji.image;

import jakarta.validation.constraints.*;

public record ImageDTO(
	@NotEmpty
	String name,

	@NotEmpty
	String url
) {
}
