package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
