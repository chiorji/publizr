package dev.chiorji.post.models;

import jakarta.validation.constraints.*;
import jdk.jfr.*;

public record Post(
	@NotEmpty
	String title,

	@NotEmpty
	String content,

	@NotNull
	String excerpt,

	@NotNull
	Integer poster_card,

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
