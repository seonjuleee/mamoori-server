package com.mamoori.mamooriback.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mamoori.mamooriback.api.entity.Will;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WillResponse {
    private Long id;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public WillResponse(Will will) {
        this.id = will.getWillId();
        this.title = will.getTitle();
        this.content = will.getContent();
        this.createdAt = will.getCreateAt();
        this.updatedAt = will.getUpdateAt();
    }

    @QueryProjection
    public WillResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
