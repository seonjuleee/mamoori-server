package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistDto;
import com.mamoori.mamooriback.api.dto.ChecklistPageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface UserChecklistRepositoryCustom {
    ChecklistPageResponse getChecklistPage(String email, Pageable pageable);
    List<ChecklistDto> getChecklist(Long userChecklistId);
    Long getTotalTaskCount(Long userChecklistId);
    Long getCheckedTaskCount(Long userChecklistId);
    LocalDateTime findLastChecklistAnswerByEmail(String email);
}
