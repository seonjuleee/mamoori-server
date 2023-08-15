package com.mamoori.mamooriback.api.dto;

import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.entity.Will;
import lombok.Getter;

@Getter
public class WillRequest {
    private String title;
    private String content;

    public Will toEntity(User user) {
        return Will.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
