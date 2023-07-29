package com.mamoori.mamooriback.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final int code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.error = errorCode.name();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
