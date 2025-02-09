package dev.publizr.post.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Post(
	@NotEmpty
	String title,

	@NotEmpty
	String content,

	String excerpt,

	String poster_card,

	String tags,

	@NotEmpty
	String status,

	@NotEmpty
	@NotNull
	Integer author_id,

	@NotEmpty
	@NotNull
	String category,

	@NotNull
	Boolean featured
) {
}
