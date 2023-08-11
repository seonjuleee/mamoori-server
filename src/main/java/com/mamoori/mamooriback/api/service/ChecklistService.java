package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistTaskResponse;
import com.mamoori.mamooriback.api.dto.ChecklistRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChecklistService {
    List<ChecklistTaskResponse> getChecklistTasks();
    ChecklistAnswerResponse getChecklistLastAnswerByEmail(String email);
    @Transactional
    void createChecklist(String email, List<ChecklistRequest> checklistRequests);
    void deleteUserChecklist(String email, Long userChecklistId);
}
