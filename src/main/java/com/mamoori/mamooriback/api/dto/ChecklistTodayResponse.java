package com.mamoori.mamooriback.api.dto;

import lombok.Getter;

@Getter
public class ChecklistTodayResponse {
    private Boolean isTodayChecklistCreated;

    public ChecklistTodayResponse(boolean isTodayChecklistCreated) {
        this.isTodayChecklistCreated = isTodayChecklistCreated;
    }
}
