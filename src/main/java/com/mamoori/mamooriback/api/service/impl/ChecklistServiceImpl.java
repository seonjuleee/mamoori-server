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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public ChecklistPageResponse getChecklists(String email, Pageable pageable) {
        ChecklistPageResponse checklistPage = userChecklistRepository.getChecklistPage(email, pageable);
        for (ChecklistResponse checklist : checklistPage.getChecklists()) {
            Long totalTaskCount = userChecklistRepository.getTotalTaskCount(checklist.getId());
            Long checkedTaskCount = userChecklistRepository.getCheckedTaskCount(checklist.getId());
            List<ChecklistDto> dto = userChecklistRepository.getChecklist(checklist.getId());
            checklist.setTotalTaskCount(totalTaskCount);
            checklist.setCheckedTaskCount(checkedTaskCount);
            checklist.setChecklist(dto);
        }

        log.debug("checklists : {}", checklistPage);
        return checklistPage;
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
        log.debug("createChecklist called...");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        log.debug("user : {}", user.getEmail());
        LocalDateTime lastChecklistDateTime = userChecklistRepository.findLastChecklistAnswerByEmail(email);
        log.debug("lastChecklistDateTime : {}", lastChecklistDateTime);

        if (lastChecklistDateTime != null && isTodayDate(lastChecklistDateTime)) {
            throw new BusinessException(ErrorCode.CHECKLIST_ALREADY_EXISTS_FOR_TODAY, ErrorCode.CHECKLIST_ALREADY_EXISTS_FOR_TODAY.getMessage());
        }

        UserChecklist saveUserChecklist = userChecklistRepository.save(new UserChecklist(user));
        log.debug("createChecklist -> userChecklistId : {}", saveUserChecklist.getUserChecklistId());

        // 전체 체크리스트 항목 가져오기
        List<Long> taskIds = getChecklistTasks().stream().map(task -> task.getId()).collect(Collectors.toList());

        // checklistRequests에 isChecked가 false인 값 추가
        for (Long taskId : taskIds) {
            if (!contains(checklistRequests, taskId)) {
                checklistRequests.add(new ChecklistRequest(taskId, false));
            }
        }

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

    private boolean isTodayDate(LocalDateTime localDateTime) {
        if (localDateTime.toLocalDate().isEqual(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean contains(List<ChecklistRequest> requests, Long taskId) {
        for (ChecklistRequest checklistRequest : requests) {
            if (taskId == checklistRequest.getId()) {
                return true;
            }
        }
        return false;
    }
}
