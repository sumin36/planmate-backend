package com.sumin.planmate.exception;

import com.sumin.planmate.util.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(NotFoundException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.of(
                404, "NOT_FOUND", e.getMessage(),request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.of(
                401, "UNAUTHORIZED", e.getMessage(),request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.of(
                409, "CONFLICT", e.getMessage(),request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).body(error);
    }

}
