package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.*;
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
    public List<ChecklistTaskResponse> getChecklistTasks() {
        return checklistRepository.getChecklistTasks();
    }

    @Override
    public ChecklistResponse getChecklistByEmailAndUserChecklistId(String email, Long userChecklistId) {
        UserChecklist userChecklist = userChecklistRepository.findById(userChecklistId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_REQUEST, ErrorCode.INVALID_REQUEST.getMessage()
                ));

        if (!email.equals(userChecklist.getUser().getEmail())){
            throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
        }

        Long totalTaskCount = userChecklistRepository.getTotalTaskCount(userChecklistId);
        Long checkedTaskCount = userChecklistRepository.getCheckedTaskCount(userChecklistId);

        List<ChecklistDto> checklist = userChecklistRepository.getChecklist(userChecklistId);

        return ChecklistResponse.builder()
                .id(userChecklist.getUserChecklistId())
                .totalTaskCount(totalTaskCount)
                .checkedTaskCount(checkedTaskCount)
                .createdAt(userChecklist.getCreateAt())
                .checklist(checklist)
                .build();
    }

    @Transactional
    @Override
    public void createChecklist(String email, List<ChecklistRequest> checklistRequests) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        UserChecklist saveUserChecklist = userChecklistRepository.save(new UserChecklist(user));
        log.debug("createChecklist -> userChecklistId : {}", saveUserChecklist.getUserChecklistId());

        for (ChecklistRequest checklistRequest : checklistRequests) {
            Checklist findChecklist = checklistRepository.findById(checklistRequest.getId()).get();
            log.debug("createChecklist -> findChecklist : {}", findChecklist.getChecklistId());
            UserChecklistAnswer saveAnswer = userChecklistAnswerRepository.save(checklistRequest.toEntity(saveUserChecklist, findChecklist));
            log.debug("createChecklist -> saveAnswer : {}", saveAnswer.getAnswerId());
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
