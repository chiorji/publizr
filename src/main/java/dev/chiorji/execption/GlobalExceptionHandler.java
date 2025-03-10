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

	@ExceptionHandler(value = PublishFailedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorDTO> handleAllException(PublishFailedException e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = JWTDecodeException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorDTO> handleJWTDecodeException(JWTDecodeException e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid JWT token", new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = TokenExpiredException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Provided JWT token expired", new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(value = JWTVerificationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<?> handleJWTVerificationException(JWTVerificationException e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "JWT verification failed", new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleGlobalException(Exception e) {
		ErrorDTO errorDTO = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", new Date());
		return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
