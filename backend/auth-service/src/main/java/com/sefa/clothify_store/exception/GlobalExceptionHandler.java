package com.sefa.clothify_store.exception;

import com.sefa.clothify_store.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private String getRequestId() {
        return MDC.get("requestId");
    }

    @ExceptionHandler(EmailMessagingException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailMessagingException
            (EmailMessagingException ex, HttpServletRequest request, HttpServletResponse response){
        log.warn("Exception -> {}", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(InvalidVerificationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidVerificationException
            (InvalidVerificationException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(WrongAuthenticationProviderException.class)
    public ResponseEntity<ErrorResponseDTO> handleWrongAuthProviderException
            (WrongAuthenticationProviderException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {

        log.warn("Exception -> {}", ex.getMessage());

        String finalMessage =ex.getBindingResult().getAllErrors().stream()
                .map(FieldError->FieldError.getDefaultMessage())
                .collect(Collectors.joining("|"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(finalMessage)
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(GoogleAuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleGoogleAuthenticationException
            (GoogleAuthenticationException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }
    @ExceptionHandler(MissingProviderAttributesException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingProviderAttributesException
            (MissingProviderAttributesException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResourceException
            (DuplicateResourceException ex,HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.CONFLICT.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException
            (ResourceNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Exception -> {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException
            (RuntimeException ex, HttpServletRequest request, HttpServletResponse response){
        log.error("Beklenmeyen bir hata oluştu.", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException
            (Exception ex, HttpServletRequest request, HttpServletResponse response) {
        log.error("Beklenmeyen bir hata oluştu.", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO
                        .builder()
                        .time(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .requestId(getRequestId())
                        .path(request.getRequestURI())
                        .build());
    }
}

