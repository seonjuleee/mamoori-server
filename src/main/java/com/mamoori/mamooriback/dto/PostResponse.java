package com.mamoori.mamooriback.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String receiver;
    private Long views;
    private Long userId;
    private Long categoryId;
    private String createAt;
    private String updateAt;
}
