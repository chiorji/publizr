package dev.chiorji.execption;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = User404Exception.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponseDTO> handleUser404Exception(User404Exception e, WebRequest request) {
		ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
		return new ResponseEntity<ErrorResponseDTO>(responseDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = AuthenticationFailedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponseDTO> handleAuthenticationFailedException(AuthenticationFailedException e) {
		ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = Post404Exception.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponseDTO> handlePost404Exception(Post404Exception e) {
		ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
	}
}
