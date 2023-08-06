package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChecklistService {
    List<ChecklistResponse> getChecklistItems();
    ChecklistAnswerResponse getChecklistLastAnswerByEmail(String email);
    @Transactional
    void putChecklistAnswer(String email, List<UserChecklistAnswerRequest> userChecklistAnswerRequestList);
    void deleteUserChecklist(String email, Long userChecklistId);
}
