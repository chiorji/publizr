package dev.chiorji.user.models;

import jakarta.validation.constraints.*;

public record LoginUpResponseDTO(
	@NotEmpty String token,

	@NotEmpty UserDTO data
) {}
