package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.WillPageResponse;
import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.mamoori.mamooriback.api.service.WillService;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class WillController {

    private final WillService willService;
    private final JwtService jwtService;

    @GetMapping("/will")
    public ResponseEntity<WillPageResponse> getWills(
            HttpServletRequest request,
            @RequestParam(required = false, value = "keyword") String title,
            @PageableDefault(size=10, sort="updateAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("getWills called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("getWills -> email : {}", email);

        WillPageResponse willResponsePage = willService.getWillListByEmail(email, title, pageable);
        return ResponseEntity.ok()
                .body(willResponsePage);
    }

    @GetMapping("/will/{id}")
    public ResponseEntity<WillResponse> getWill(@PathVariable("id") Long willId,
                                                HttpServletRequest request) {
        log.debug("getWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("getWill -> email : {}", email);
        WillResponse willResponse = willService.getWillById(email, willId);

        return ResponseEntity.ok()
                .body(willResponse);
    }

    @PutMapping("/will")
    public ResponseEntity putWill(@RequestBody WillRequest willRequest,
                                   HttpServletRequest request) {
        log.debug("putWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("putWill -> email : {}", email);
        willService.putWill(email, willRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/will/{id}")
    public ResponseEntity deleteWill(@PathVariable("id") Long willId,
                                     HttpServletRequest request) {
        log.debug("deleteWill called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));
        log.debug("deleteWill -> email : {}", email);
        willService.deleteWill(email, willId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
