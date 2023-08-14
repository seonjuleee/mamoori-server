package com.mamoori.mamooriback.auth.controller;

import com.mamoori.mamooriback.auth.dto.TokenResponse;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import com.mamoori.mamooriback.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity createToken(HttpServletRequest request, HttpServletResponse response) {
        log.debug("createToken called...");

        // access token 재발급
        String refreshToken = CookieUtil.getCookie(request, jwtService.getRefreshHeader())
                .map(Cookie::getValue)
                .orElse((null));
        log.debug("refreshToken : {}", refreshToken);

        if (!jwtService.isTokenValid(refreshToken)) {
            // 오류 처리
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN, ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
        }

        TokenResponse tokenResponse = jwtService.reissueAccessTokenByRefreshToken(refreshToken);

        jwtService.setAccessTokenCookie(response, tokenResponse.getAccessToken());
        jwtService.setRefreshTokenCookie(response, tokenResponse.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/token")
    public ResponseEntity deleteToken(HttpServletRequest request, HttpServletResponse response) {
        log.debug("deleteToken called...");

        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()
                ));


        jwtService.reissueAccessTokenByAccessToken(accessToken);

        CookieUtil.deleteCookie(request, response, jwtService.getRefreshHeader(), true);
        CookieUtil.deleteCookie(request, response, jwtService.getAccessHeader(), false);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
