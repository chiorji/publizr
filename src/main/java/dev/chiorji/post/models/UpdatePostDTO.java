package dev.chiorji.post.models;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.*;

public record UpdatePostDTO(
	@Id
	Integer id,

	@NotNull
	Integer author_id,

	@NotEmpty
	String title,

	@NotEmpty
	String excerpt,

	@NotEmpty
	String content,

	@NotNull
	String tags,

	@NotNull
	Integer category,

	@NotEmpty
	String status
) {
}
