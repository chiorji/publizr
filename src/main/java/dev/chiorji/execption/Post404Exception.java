package dev.chiorji.execption;

public class Post404Exception extends RuntimeException {
	public Post404Exception() {
	}

	public Post404Exception(String message) {
		super(message);
	}
}
