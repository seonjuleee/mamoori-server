package com.mamoori.mamooriback.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mamoori.mamooriback.api.entity.Will;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WillResponse {
    private Long willId;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateAt;

    public WillResponse(Will will) {
        this.willId = will.getWillId();
        this.title = will.getTitle();
        this.content = will.getContent();
        this.createAt = will.getCreateAt();
        this.updateAt = will.getUpdateAt();
    }

    @QueryProjection
    public WillResponse(Long willId, String title, String content, LocalDateTime createAt, LocalDateTime updateAt) {
        this.willId = willId;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
