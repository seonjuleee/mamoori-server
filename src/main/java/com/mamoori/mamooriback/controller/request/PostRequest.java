package com.mamoori.mamooriback.controller.request;

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
    private String categoryId; // Category
    private Long userId; // User
}
