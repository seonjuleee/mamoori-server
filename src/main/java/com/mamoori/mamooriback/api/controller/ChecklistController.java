package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.ChecklistAnswerResponse;
import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.dto.UserChecklistAnswerRequest;
import com.mamoori.mamooriback.api.service.ChecklistService;
import com.mamoori.mamooriback.auth.service.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ChecklistController {

    private final ChecklistService checklistService;

    @GetMapping("/checklist/items")
    public ResponseEntity<List<ChecklistResponse>> getChecklistItems() {
        log.debug("getChecklistItems called...");

        List<ChecklistResponse> checklistItems = checklistService.getChecklistItems();
        return ResponseEntity.ok()
                .body(checklistItems);
    }

    @GetMapping("/checklist/last-answer")
    public ResponseEntity<ChecklistAnswerResponse> getChecklistAnswer(
            @AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal) {
        log.debug("getChecklistAnswer called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("getChecklistAnswer -> email : {}", email);

        ChecklistAnswerResponse checklistAnswerResponse = checklistService.getChecklistLastAnswerByEmail(email);
        return ResponseEntity.ok()
                .body(checklistAnswerResponse);
    }

    @PutMapping("/checklist/answers")
    public ResponseEntity putChecklistAnswer(
            @RequestBody List<UserChecklistAnswerRequest> userChecklistAnswerRequests,
            @AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal) {
        log.debug("putChecklistAnswer called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("putChecklistAnswer -> email : {}", email);

        checklistService.putChecklistAnswer(email, userChecklistAnswerRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
