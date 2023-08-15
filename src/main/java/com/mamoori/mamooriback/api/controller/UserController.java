package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.UserResponse;
import com.mamoori.mamooriback.api.service.UserService;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request) {
        log.debug("getUser called...");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        String email = jwtService.extractEmailByAccessToken(accessToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));

        log.debug("getUser -> email : {}", email);

        UserResponse userResponse = userService.getUserResponseByEmail(email);
        return ResponseEntity.ok()
                .body(userResponse);
    }

}
