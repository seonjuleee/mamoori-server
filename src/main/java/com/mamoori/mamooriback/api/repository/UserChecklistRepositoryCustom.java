package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerResponse;

import java.util.List;

public interface UserChecklistRepositoryCustom {
    ChecklistAnswerResponse findLastChecklistAnswerByEmail(String email);
    List<UserChecklistAnswerResponse> findUserChecklistAnswersByUserChecklistId(Long userChecklistId);
}
