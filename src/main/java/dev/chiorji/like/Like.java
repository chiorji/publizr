package dev.chiorji.like;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.*;

public record Like(
	@Id
	Integer id,

	@NotNull
	Integer post_id,

	@NotNull
	Integer user_id
) {
}
