package dev.chiorji.image;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

public record ImageUploadDTO(
	@NotEmpty
	String name,
	@NotEmpty
	MultipartFile url
) {
}
