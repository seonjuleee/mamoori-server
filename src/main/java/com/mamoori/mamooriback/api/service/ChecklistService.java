package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChecklistService {
    List<ChecklistTaskResponse> getChecklistTasks();
    @Transactional
    void createChecklist(String email, List<ChecklistRequest> checklistRequests);
    void deleteUserChecklist(String email, Long userChecklistId);
}
