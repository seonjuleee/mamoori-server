package com.mamoori.mamooriback.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChecklistResponse {
    private Long checklistId;
    private String description;
    private Integer order;

    @QueryProjection
    public ChecklistResponse(Long checklistId, String description, Integer order) {
        this.checklistId = checklistId;
        this.description = description;
        this.order = order;
    }
}
