package dev.chiorji.post.models;

import jakarta.validation.constraints.*;

public record PostDeleteDTO(
	@NotNull
	Integer id,

	@NotNull
	Integer author_id
) {
}
