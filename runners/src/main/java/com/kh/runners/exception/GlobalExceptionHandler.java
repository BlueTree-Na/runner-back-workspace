package com.kh.runners.exception;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

	// 입력값 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }
	
	
	
	// 빈문자열
	@ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidParameter(InvalidParameterException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
	
	// 중복 체크 예외 처리 (아이디 / 닉네임 중복)
	@ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity.badRequest().body(Map.of("duplicate", e.getMessage()));
    }
	
	// 비밀번호 검증 예외 처리
    @ExceptionHandler(MissmatchPasswordException.class)
    public ResponseEntity<Map<String, String>> handleMissmatchPassword(MissmatchPasswordException e) {
        return ResponseEntity.badRequest().body(Map.of("passwordError", e.getMessage()));
    }
	
    //인증 예외 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException e) {
        return ResponseEntity.badRequest().body(Map.of("authError", e.getMessage()));
    }
	
}
