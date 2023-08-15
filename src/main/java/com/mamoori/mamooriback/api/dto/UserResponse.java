package com.mamoori.mamooriback.api.dto;

import com.mamoori.mamooriback.api.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long userId;
    private String email;
    private String name;

    private String image;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.image = user.getImage();
    }
}
