package dev.chiorji.execption;

import java.util.*;

public record ErrorResponseDTO(
	Integer statusCode,
	String message,
	Date timestamp
) {
}
