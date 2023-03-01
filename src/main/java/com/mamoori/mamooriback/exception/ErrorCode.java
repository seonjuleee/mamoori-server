package com.mamoori.mamooriback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // HTTP
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    TOO_MANY_REQUESTS(429, HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다."),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    // Common
    INVALID_REQUEST(1001, HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),

    // User
    USER_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(2002, HttpStatus.BAD_REQUEST, "회원이 이미 존재합니다."),

    ;

    private final int code;
    private final HttpStatus status;
    private final String message;
}
