package com.mamoori.mamooriback.api.dto;

import com.mamoori.mamooriback.api.entity.Checklist;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import com.mamoori.mamooriback.api.entity.UserChecklistAnswer;
import lombok.Getter;

@Getter
public class ChecklistRequest {
    private Long id;
    private Boolean isChecked;

    public UserChecklistAnswer toEntity(UserChecklist userChecklist, Checklist checklist) {
        return UserChecklistAnswer.builder()
                .isCheck(this.isChecked)
                .userChecklist(userChecklist)
                .checklist(checklist)
                .build();
    }

    public ChecklistRequest() {
    }

    public ChecklistRequest(Long id, Boolean isChecked) {
        this.id = id;
        this.isChecked = isChecked;
    }
}
