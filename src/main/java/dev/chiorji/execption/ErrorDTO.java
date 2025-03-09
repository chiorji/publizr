package dev.chiorji.execption;

import java.util.*;

public record ErrorDTO(
	Integer statusCode,
	String message,
	Date timestamp
) {
}
