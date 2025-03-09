package dev.chiorji.execption;

public class AuthenticationFailedException extends RuntimeException {
	public AuthenticationFailedException() {
	}

	public AuthenticationFailedException(String message) {
		super(message);
	}
}
