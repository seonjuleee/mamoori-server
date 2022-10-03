package com.mamoori.mamooriback.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResDto {
    private String title;
    private String content;
    private String receiver;
    private String createAt;
    private String updateAt;
}
