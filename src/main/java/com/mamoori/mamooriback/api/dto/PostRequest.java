package com.mamoori.mamooriback.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private Long categoryId;
}
