package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistResponse;

import java.util.List;

public interface ChecklistService {
    List<ChecklistResponse> getChecklistItems();
    ChecklistAnswerResponse getChecklistLastAnswerByEmail(String email);
}
