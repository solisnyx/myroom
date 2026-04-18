package com.myroom.common;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(),
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(),
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(),
                LocaleContextHolder.getLocale());
        return ResponseEntity.badRequest().body(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();
        String message = messageSource.getMessage("error.validation", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.badRequest().body(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(fieldErrors)
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        String message = messageSource.getMessage("error.internal", null,
                LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private ApiError.FieldError toFieldError(FieldError fe) {
        return ApiError.FieldError.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build();
    }
}
