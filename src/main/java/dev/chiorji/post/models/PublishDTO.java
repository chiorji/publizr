package dev.chiorji.post.models;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

public record PublishDTO(
	@NotEmpty
	String title,
	@NotEmpty
	String content,
	String excerpt,
	@NotNull
	MultipartFile poster_card,
	@NotNull
	String tags,
	@NotEmpty
	String status,
	@NotNull
	Integer author_id,
	@NotNull
	Integer category
) {}
