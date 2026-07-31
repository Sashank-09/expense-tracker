package com.diligent.expensetracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ExpenseNotFoundException ex,
            HttpServletRequest request) {

        logger.warn(
                "Expense not found. Path={}, Message={}",
                request.getRequestURI(),
                ex.getMessage()
        );

        ApiError error = buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ValidationError> fieldErrors =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                new ValidationError(
                                        error.getField(),
                                        error.getDefaultMessage()))
                        .toList();

        logger.warn(
                "Validation failed. Path={}, Errors={}",
                request.getRequestURI(),
                fieldErrors
        );

        ApiError apiError = buildError(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        logger.error(
                "Unexpected exception while processing request. Path={}",
                request.getRequestURI(),
                ex
        );

        ApiError error = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.",
                request.getRequestURI(),
                Collections.emptyList()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    private ApiError buildError(
            HttpStatus status,
            String message,
            String path,
            List<ValidationError> fieldErrors) {

        return new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                fieldErrors
        );
    }
}