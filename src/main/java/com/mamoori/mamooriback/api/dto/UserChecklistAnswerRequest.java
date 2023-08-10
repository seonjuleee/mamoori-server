package com.mamoori.mamooriback.api.dto;

import com.mamoori.mamooriback.api.entity.Checklist;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import com.mamoori.mamooriback.api.entity.UserChecklistAnswer;
import lombok.Getter;

@Getter
public class UserChecklistAnswerRequest {
    private Long checklistId;
    private Boolean answer;

    public UserChecklistAnswer toEntity(UserChecklist userChecklist, Checklist checklist) {
        return UserChecklistAnswer.builder()
                .isCheck(this.answer)
                .userChecklist(userChecklist)
                .checklist(checklist)
                .build();
    }
}
