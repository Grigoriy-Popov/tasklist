package com.example.tasklist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDeniedException() {
        return handleException("Access denied");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(AuthenticationException e) {
        return handleException("Bad authentication");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotFoundException(NotFoundException e) {
        return handleException(e.getMessage());
    }

    @ExceptionHandler(IncorrectTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleIncorrectTokenException(IncorrectTokenException e) {
        return handleException(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldErrors().get(0);
        return handleException("Field [%s]. Error - %s".formatted(error.getField(), error.getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("Exception: ");
        e.printStackTrace();
        return handleException("Что-то пошло не так");
    }

    private ApiError handleException(String message) {
        log.error(message);
        return ApiError.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
