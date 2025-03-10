package dev.chiorji.execption;

public class PublishFailedException extends RuntimeException {
	public PublishFailedException() {
	}

	public PublishFailedException(String message) {
		super(message);
	}
}
