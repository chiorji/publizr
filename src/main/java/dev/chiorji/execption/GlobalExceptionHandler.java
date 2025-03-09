package dev.chiorji.execption;

import com.auth0.jwt.exceptions.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = User404Exception.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorDTO> handleUser404Exception(User404Exception e, WebRequest request) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = AuthenticationFailedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorDTO> handleAuthenticationFailedException(AuthenticationFailedException e) {
		ErrorDTO responseDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = Post404Exception.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorDTO> handlePost404Exception(Post404Exception e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
	}

//	@ExceptionHandler(value = Exception.class)
//	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//	public ResponseEntity<ErrorDTO> handleAllException(Exception e) {
//		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), new Date());
//		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_ACCEPTABLE);
//	}

	@ExceptionHandler(value = JWTDecodeException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorDTO> handleJWTDecodeException(JWTDecodeException e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Invalid token", new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
	}
}
