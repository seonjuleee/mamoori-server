package com.mamoori.mamooriback.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChecklistTaskResponse {
    private Long id;
    private String task;
    private Integer order;

    @QueryProjection
    public ChecklistTaskResponse(Long id, String task, Integer order) {
        this.id = id;
        this.task = task;
        this.order = order;
    }
}
