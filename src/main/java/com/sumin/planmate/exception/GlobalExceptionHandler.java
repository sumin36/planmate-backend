package com.sumin.planmate.exception;

import com.sumin.planmate.util.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(NotFoundException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.generalError(404, "NOT_FOUND", e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.generalError(401, "UNAUTHORIZED", e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.generalError(409, "CONFLICT", e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidRoutineException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoutineException(InvalidRoutineException e, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.generalError(400, "BAD_REQUEST", e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> errors =
                e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponse error = ErrorResponse.validationError(request.getRequestURI(), errors);
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(error);
    }
}
