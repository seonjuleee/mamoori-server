package com.mamoori.mamooriback.api.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserChecklistRepositoryCustom {
    List<ChecklistDto> getChecklist(Long userChecklistId);
    Long getTotalTaskCount(Long userChecklistId);
    Long getCheckedTaskCount(Long userChecklistId);
}
