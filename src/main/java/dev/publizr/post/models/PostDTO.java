package dev.publizr.post.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record PostDTO(
	@NotEmpty
	String username,

	@Id
	@NotNull
	Integer author_id,

	@Id
	@NotNull
	Integer post_id,

	@NotEmpty
	String title,

	@Nullable
	String excerpt,

	@NotEmpty
	String content,

	@DateTimeFormat
	LocalDateTime posted_on,

	@DateTimeFormat
	LocalDateTime last_updated,

	@NotEmpty
	String category,

	@Nullable
	String tags,

	@NotNull
	String poster_card,

	@NotEmpty
	String status,

	@NotNull
	Boolean featured
) {
}
