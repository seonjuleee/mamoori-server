package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.UserResponse;
import com.mamoori.mamooriback.api.service.UserService;
import com.mamoori.mamooriback.auth.service.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2UserPrincipal) {
        log.debug("getUser called...");
        String email = oAuth2UserPrincipal.getEmail();
        log.debug("getUser -> email : {}", email);

        UserResponse userResponse = userService.getUserResponseByEmail(email);
        return ResponseEntity.ok()
                .body(userResponse);
    }

}
