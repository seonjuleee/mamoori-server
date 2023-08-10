package com.mamoori.mamooriback.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserChecklistAnswerResponse {
    private Long checklistId;
    private Boolean answer;

    @QueryProjection
    public UserChecklistAnswerResponse(Long checklistId, Boolean answer) {
        this.checklistId = checklistId;
        this.answer = answer;
    }
}
