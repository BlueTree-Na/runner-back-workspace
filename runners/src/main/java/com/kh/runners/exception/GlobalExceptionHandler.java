package com.kh.runners.exception;

import java.security.InvalidParameterException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

	// 빈문자열
	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<?> handleInvalidParameter(InvalidParameterException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	// 중복 
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<?> handleDuplicateUser(DuplicateUserException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	// 비밀번호 검증
	@ExceptionHandler(MissmatchPasswordException.class)
	public ResponseEntity<?> handleMissmatchPassword(MissmatchPasswordException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthentication(AuthenticationException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
}
