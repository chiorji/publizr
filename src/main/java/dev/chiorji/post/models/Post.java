package dev.chiorji.post.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;

public record Post(
	@NotEmpty
	String title,

	@NotEmpty
	String content,

	@NotNull
	String excerpt,

	@NotNull
	String poster_card,

	@NotNull
	String tags,

	@NotEmpty
	String status,

	@NotNull
	Integer author_id,

	@NotEmpty
	String category,

	@BooleanFlag
	Boolean featured
) {
}
