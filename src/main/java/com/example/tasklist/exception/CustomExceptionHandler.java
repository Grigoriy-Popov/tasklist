package com.example.tasklist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(AuthenticationException e) {
        return ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .reason("Authentication failed")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthenticationException(AccessDeniedException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("Not authenticate")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNoTFoundException(NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Entity was not found")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IncorrectTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleIncorrectTokenException(IncorrectTokenException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("Incorrect token")
                .message(e.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
