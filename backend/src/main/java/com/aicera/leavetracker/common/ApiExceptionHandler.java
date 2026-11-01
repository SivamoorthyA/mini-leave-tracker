package com.aicera.leavetracker.common;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> notFound(NotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> badRequest(BadRequestException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> conflict(ConflictException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> invalidPathValue(MethodArgumentTypeMismatchException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getName() + " has an invalid value");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> invalidFields(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.sorted()
				.collect(Collectors.joining(", "));
		return build(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> unreadableBody(HttpMessageNotReadableException ex) {
		return build(HttpStatus.BAD_REQUEST, "Request body is missing or has an invalid value");
	}

	private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(new ErrorResponse(status.value(), status.getReasonPhrase(), message));
	}
}
