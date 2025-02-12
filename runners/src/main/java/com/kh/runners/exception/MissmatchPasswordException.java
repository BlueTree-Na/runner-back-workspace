package com.kh.runners.exception;

public class MissmatchPasswordException extends RuntimeException{

	public MissmatchPasswordException(String message) {
		super(message);
	}
}
