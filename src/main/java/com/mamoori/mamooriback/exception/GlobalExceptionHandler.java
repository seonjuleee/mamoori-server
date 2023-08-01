package com.mamoori.mamooriback.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
                .body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException : {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(final NullPointerException e) {
        log.error("handleNullPointerException : {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus().value())
                .body(new ErrorResponse(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final Exception e) {
        log.error("handleRuntimeException : {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

