package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistDto;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface UserChecklistRepositoryCustom {
    Page<UserChecklist> getChecklistPage(String email, Pageable pageable);
    List<ChecklistDto> getChecklist(Long userChecklistId);
    Long getTotalTaskCount(Long userChecklistId);
    Long getCheckedTaskCount(Long userChecklistId);
    LocalDateTime findLastChecklistAnswerByEmail(String email);
}
