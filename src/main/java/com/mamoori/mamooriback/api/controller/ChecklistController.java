package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistTaskResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerRequest;
import com.mamoori.mamooriback.api.service.ChecklistService;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChecklistController {

    private final ChecklistService checklistService;
    private final JwtService jwtService;

    @GetMapping("/checklist/tasks")
    public ResponseEntity<List<ChecklistTaskResponse>> getChecklistTasks() {
        log.debug("getChecklistTasks called...");

        List<ChecklistTaskResponse> checklistItems = checklistService.getChecklistTasks();
        return ResponseEntity.ok()
                .body(checklistItems);
    }

    @GetMapping("/checklist/last-answer")
    public ResponseEntity<ChecklistAnswerResponse> getChecklistAnswer(
            HttpServletRequest request) {
        log.debug("getChecklistAnswer called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("getChecklistAnswer -> email : {}", email);

        ChecklistAnswerResponse checklistAnswerResponse = checklistService.getChecklistLastAnswerByEmail(email);
        return ResponseEntity.ok()
                .body(checklistAnswerResponse);
    }

    @PutMapping("/checklist/answers")
    public ResponseEntity putChecklistAnswer(
            @RequestBody List<UserChecklistAnswerRequest> userChecklistAnswerRequests,
            HttpServletRequest request) {
        log.debug("putChecklistAnswer called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("putChecklistAnswer -> email : {}", email);

        checklistService.putChecklistAnswer(email, userChecklistAnswerRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/checklist/answers/{id}")
    public ResponseEntity deleteChecklist(
            @PathVariable("id") Long userChecklistId,
            HttpServletRequest request) {
        log.debug("deleteChecklist called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("deleteChecklist -> email : {}", email);

        checklistService.deleteUserChecklist(email, userChecklistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
