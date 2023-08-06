package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerResponse;
import com.mamoori.mamooriback.api.repository.ChecklistRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistRepository;
import com.mamoori.mamooriback.api.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final UserChecklistRepository userChecklistRepository;

    @Override
    public List<ChecklistResponse> getChecklistItems() {
        return checklistRepository.getChecklistItems();
    }

    @Override
    public ChecklistAnswerResponse getChecklistLastAnswerByEmail(String email) {
        ChecklistAnswerResponse checklistAnswer = userChecklistRepository.findLastChecklistAnswerByEmail(email);
        List<UserChecklistAnswerResponse> userChecklist = userChecklistRepository.findUserChecklistAnswersByUserChecklistId(checklistAnswer.getUserChecklistId());
        checklistAnswer.setContent(userChecklist);
        return checklistAnswer;
    }

}
