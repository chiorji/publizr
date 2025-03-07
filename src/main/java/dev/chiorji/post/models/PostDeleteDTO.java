package dev.chiorji.post.models;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.*;

public record PostDeleteDTO(
	@Id
	@NotNull
	Integer id,

	@NotNull
	Integer author_id
) {
}
