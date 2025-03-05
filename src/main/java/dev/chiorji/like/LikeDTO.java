package dev.chiorji.like;

import jakarta.validation.constraints.*;

public record LikeDTO(
	@NotNull
	Integer post_id,

	@NotNull
	Integer user_id
) {
}
