package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerRequest;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerResponse;
import com.mamoori.mamooriback.api.entity.Checklist;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import com.mamoori.mamooriback.api.entity.UserChecklistAnswer;
import com.mamoori.mamooriback.api.repository.ChecklistRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistAnswerRepository;
import com.mamoori.mamooriback.api.repository.UserChecklistRepository;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.api.service.ChecklistService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final UserChecklistRepository userChecklistRepository;
    private final UserChecklistAnswerRepository userChecklistAnswerRepository;
    private final UserRepository userRepository;

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

    @Transactional
    @Override
    public void putChecklistAnswer(String email, List<UserChecklistAnswerRequest> userChecklistAnswerRequests) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        UserChecklist saveUserChecklist = userChecklistRepository.save(new UserChecklist(user));
        log.debug("putChecklistAnswer -> userChecklistId : {}", saveUserChecklist.getUserChecklistId());

        for (UserChecklistAnswerRequest answer : userChecklistAnswerRequests) {
            Checklist findChecklist = checklistRepository.findById(answer.getChecklistId()).get();
            log.debug("putChecklistAnswer -> findChecklist : {}", findChecklist.getChecklistId());
            UserChecklistAnswer saveAnswer = userChecklistAnswerRepository.save(answer.toEntity(saveUserChecklist, findChecklist));
            log.debug("putChecklistAnswer -> saveAnswer : {}", saveAnswer.getAnswerId());
        }
    }

    @Override
    public void deleteUserChecklist(String email, Long userChecklistId) {
        UserChecklist userChecklist = userChecklistRepository.findByUser_EmailAndUserChecklistId(email, userChecklistId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()
                ));
        userChecklistRepository.delete(userChecklist);
    }
}
