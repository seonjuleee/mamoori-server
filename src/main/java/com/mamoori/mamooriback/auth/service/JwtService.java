package com.mamoori.mamooriback.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mamoori.mamooriback.api.entity.Token;
import com.mamoori.mamooriback.api.repository.TokenRepository;
import com.mamoori.mamooriback.auth.dto.TokenResponse;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import com.mamoori.mamooriback.util.CookieUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    // Subject와 Claim으로 email 사용
    private static final String EMAIL_CLAIM = "email";

    // JWT Header에 들어오는 값 : 'Authorization(Key) = Bearer {token} (Value)' 형식
    private static final String BEARER = "Bearer ";

    private final TokenRepository tokenRepository;

    public String createAccessToken(String email) {
        // TODO LocalDateTime으로 변경하기
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                // claim으로 email 외 식별자나 이름 등의 정보 추가 가능
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken Claim에 email 넣지 않음
     */
    public String createRefreshToken() {
        // TODO LocalDateTime으로 변경하기
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }


    /**
     * 'Bearer {token}'에서 token만 추출
     */
    private Optional<String> extractToken(HttpServletRequest request, String header) {
        return Optional.ofNullable(request.getHeader(header))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, refreshHeader);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return extractToken(request, accessHeader);
    }

    /**
     * AccessToken에서 Email 추출
     * 1. 검증
     * 2. 유효하면 이메일 추출
     * 3. 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmailByAccessToken(String accessToken) {
        try {
            // 토큰 유효성 검사를 위한 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken) // token을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        int cookieMaxAge = (int) (accessTokenExpirationPeriod / 1000);
        CookieUtil.addCookie(response, accessHeader, accessToken, cookieMaxAge, false);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) (refreshTokenExpirationPeriod / 1000);
        CookieUtil.addCookie(response, refreshHeader, refreshToken, cookieMaxAge, true);
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            log.debug("유효한 토큰");
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public TokenResponse reissueAccessTokenByRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getMessage()));
        String reIssueAccessToken = createAccessToken(token.getUser().getEmail());
        String reIssueRefreshToken = createRefreshToken();

        log.debug("reIssueAccessToken : {}", reIssueAccessToken);
        log.debug("reIssueRefreshToken : {}", reIssueRefreshToken);

        // DB 저장
        token.setAccessToken(reIssueAccessToken);
        token.setRefreshToken(reIssueRefreshToken);
        tokenRepository.save(token);

        return TokenResponse.builder()
                .accessToken(reIssueAccessToken)
                .refreshToken(reIssueRefreshToken)
                .build();
    }
}
