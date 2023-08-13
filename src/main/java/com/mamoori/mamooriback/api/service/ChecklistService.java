package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChecklistService {
    List<ChecklistTaskResponse> getChecklistTasks();
    ChecklistPageResponse getChecklists(String email, Pageable pageable);
    ChecklistResponse getChecklistByEmailAndUserChecklistId(String email, Long userChecklistId);
    @Transactional
    void createChecklist(String email, List<ChecklistRequest> checklistRequests);
    void deleteUserChecklist(String email, Long userChecklistId);
}
