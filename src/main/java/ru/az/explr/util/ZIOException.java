package ru.az.explr.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ZIOException extends IOException{

	private static final long serialVersionUID = -7748266174149452499L;
	
	@Getter
	private final HttpStatus status;
	
	@Getter
	private final Exception exParent;
	
	public ZIOException(HttpStatus status, Exception exParent, String message) {
		super(message);
		this.status = status;
		this.exParent = exParent;
	}
	
	public ZIOException(HttpStatus status, String message) {
		this(status, new IOException(message), message);
	}
	
	public ZIOException(String message) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
	
}
