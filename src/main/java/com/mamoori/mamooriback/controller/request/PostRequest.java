package com.mamoori.mamooriback.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequest {
    private String title;
    private String category;
    // TODO User 추가
}
