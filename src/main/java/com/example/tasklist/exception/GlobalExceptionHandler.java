package com.example.tasklist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ApiError handleAuthenticationException(AuthenticationException e) {
//        return handleException(e.getMessage());
//    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthenticationException() {
        return handleException("Access denied");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNoTFoundException(NotFoundException e) {
        return handleException(e.getMessage());
    }

    @ExceptionHandler(IncorrectTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleIncorrectTokenException(IncorrectTokenException e) {
        return handleException(e.getMessage());
    }

    private ApiError handleException(String message) {
        return ApiError.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
