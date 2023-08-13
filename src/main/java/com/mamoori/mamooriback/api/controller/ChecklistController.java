package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.*;
import com.mamoori.mamooriback.api.service.ChecklistService;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/checklist")
    public ResponseEntity<ChecklistPageResponse> getChecklists(
            HttpServletRequest request,
            @PageableDefault(size=10, sort="createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("getChecklists called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("getChecklists -> email : {}", email);

        ChecklistPageResponse checklists = checklistService.getChecklists(email, pageable);

        return ResponseEntity.ok()
                .body(checklists);
    }


    @GetMapping("/checklist/{id}")
    public ResponseEntity<ChecklistResponse> getChecklist(
            HttpServletRequest request,
            @PathVariable("id") Long userChecklistId) {
        log.debug("getChecklist called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("getChecklist -> email : {}", email);

        ChecklistResponse checklistResponse = checklistService.getChecklistByEmailAndUserChecklistId(email, userChecklistId);

        return ResponseEntity.ok()
                .body(checklistResponse);
    }

    @PostMapping("/checklist")
    public ResponseEntity postChecklistAnswer(
            @RequestBody List<ChecklistRequest> checklistRequests, HttpServletRequest request) {
        log.debug("postChecklistAnswer called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("postChecklistAnswer -> email : {}", email);

        checklistService.createChecklist(email, checklistRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/checklist/{id}")
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
