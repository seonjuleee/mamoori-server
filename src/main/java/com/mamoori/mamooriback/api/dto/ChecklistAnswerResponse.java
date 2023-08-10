package com.mamoori.mamooriback.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ChecklistAnswerResponse {
    private Long userChecklistId;

    private List<UserChecklistAnswerResponse> content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;

    @QueryProjection
    public ChecklistAnswerResponse(Long userChecklistId, LocalDateTime createAt) {
        this.userChecklistId = userChecklistId;
        this.createAt = createAt;
    }
}
