package dev.publizr.post.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PostDTO(
	@NotEmpty
	@NotNull
	String username,

	Integer author_id,

	Integer post_id,

	String title,

	String excerpt,

	@NotEmpty
	@NotNull
	String content,

	@NotEmpty
	@Nullable
	LocalDateTime posted_on,

	@NotEmpty
	@Nullable
	LocalDateTime last_updated,

	@NotEmpty
	@NotNull
	String category,

	String tags,

	@NotEmpty
	@NotNull
	String poster_card,

	@NotEmpty
	String status,

	@NotNull
	Boolean featured
) {
}
