package com.mamoori.mamooriback.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChecklistDto {
    private Long id;
    private Boolean isChecked;
    private String task;


    @QueryProjection
    public ChecklistDto(Long id, Boolean isChecked, String task) {
        this.id = id;
        this.isChecked = isChecked;
        this.task = task;
    }
}
