package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.PageResponse;
import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.mamoori.mamooriback.api.service.WillService;
import com.mamoori.mamooriback.auth.service.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class WillController {

    private final WillService willService;

    @GetMapping("/wills")
    public ResponseEntity<PageResponse<WillResponse>> getWills(
            @AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal,
            @RequestParam(required = false, value = "keyword") String title,
            @PageableDefault(size=10, sort="updateAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("getWills called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("getWills -> email : {}", email);

        PageResponse<WillResponse> willResponsePage = willService.getWillListByEmail(email, title, pageable);
        return ResponseEntity.ok()
                .body(willResponsePage);
    }

    @GetMapping("/wills/{id}")
    public ResponseEntity<WillResponse> getWill(@PathVariable("id") Long willId,
                                                @AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal) {
        log.debug("getWill called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("getWill -> email : {}", email);
        WillResponse willResponse = willService.getWillById(email, willId);

        return ResponseEntity.ok()
                .body(willResponse);
    }

    @PutMapping("/wills")
    public ResponseEntity putWill(@RequestBody WillRequest willRequest,
                                  @AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal) {
        log.debug("putWill called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("putWill -> email : {}", email);
        willService.putWill(email, willRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
