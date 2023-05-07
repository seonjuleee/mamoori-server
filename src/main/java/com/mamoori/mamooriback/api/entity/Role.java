package com.mamoori.mamooriback.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "손님"),
    ADMIN("ROLE_ADMIN","관리자");

    private String code;
    private String name;
}