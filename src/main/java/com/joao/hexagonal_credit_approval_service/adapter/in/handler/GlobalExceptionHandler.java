package com.joao.hexagonal_credit_approval_service.adapter.in.handler;

import com.joao.hexagonal_credit_approval_service.adapter.exception.NotFoundException;
import com.joao.hexagonal_credit_approval_service.adapter.exception.SerializeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> randomException(Exception exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                request.getRequestURI(),
                LocalDateTime.now().toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFoundException(RuntimeException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                request.getRequestURI(),
                LocalDateTime.now().toString());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SerializeException.class)
    public ResponseEntity<ApiError> serializeException(RuntimeException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                request.getRequestURI(),
                LocalDateTime.now().toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
