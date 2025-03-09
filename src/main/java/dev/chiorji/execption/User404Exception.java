package dev.chiorji.execption;

public class User404Exception extends RuntimeException {

	public User404Exception() {
	}

	public User404Exception(String message) {
		super(message);
	}
}
