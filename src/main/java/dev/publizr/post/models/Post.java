package dev.publizr.post.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;

public record Post(
	@NotEmpty
	String title,

	@NotEmpty
	String content,

	@Nullable
	String excerpt,

	@Nullable
	String poster_card,

	@Nullable
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
