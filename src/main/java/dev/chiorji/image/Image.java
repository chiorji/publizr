package dev.chiorji.image;

import jakarta.validation.constraints.*;

public record Image(
	@NotNull
	Integer id,
	@NotEmpty
	String name,
	@NotEmpty
	String url,
	@NotEmpty
	String asset_id
) {
}
