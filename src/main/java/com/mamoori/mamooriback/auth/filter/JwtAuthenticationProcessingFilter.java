package com.mamoori.mamooriback.auth.filter;

import com.google.gson.Gson;
import com.mamoori.mamooriback.api.entity.Token;
import com.mamoori.mamooriback.api.repository.TokenRepository;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.auth.service.JwtService;
import com.mamoori.mamooriback.exception.ErrorCode;
import com.mamoori.mamooriback.exception.ErrorResponse;
import com.mamoori.mamooriback.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    /**
     * token 처리 필터
     * 1. Access Token 유효성 검사
     * 2-1. 유효하면 API 진행
     * 2-2. 유효하지 않거나 만료 시 Refresh Token으로 재발급 (Refresh Token도 재발급)
     * 2-3. Refresh Token 유효하지 않으면 로그인 URI로 Redirect
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        Optional<String> optionalAccessToken = CookieUtil.getCookie(request, jwtService.getAccessHeader())
                .map(Cookie::getValue)
                .filter(jwtService::isTokenValid);
        if (optionalAccessToken.isPresent()) {
            // API 처리
            jwtService.extractEmailByAccessToken(optionalAccessToken.get())
                    .ifPresent(email -> userRepository.findByEmail(email));
            filterChain.doFilter(request, response);
            return;
        } else {
            // access token 재발급
            String refreshToken = CookieUtil.getCookie(request, jwtService.getRefreshHeader())
                    .map(Cookie::getValue)
                    .orElse((null));
            log.debug("refreshToken : {}", refreshToken);

            if (jwtService.isTokenValid(refreshToken)) {
                // 재발급
                Optional<Token> optionalToken = tokenRepository.findByRefreshToken(refreshToken);
                if (optionalToken.isPresent()) {
                    Token token = optionalToken.get();
                    log.debug("token : {}, {}", token.getAccessToken(), token.getRefreshToken());
                    String reIssueAccessToken = jwtService.createAccessToken(token.getUser().getEmail());
                    String reIssueRefreshToken = jwtService.createRefreshToken();

                    log.debug("token : {}, {}", reIssueAccessToken, reIssueRefreshToken);

                    // DB 저장
                    token.setAccessToken(reIssueAccessToken);
                    token.setRefreshToken(reIssueRefreshToken);
                    tokenRepository.save(token);

                    // 응답값 설정
                    jwtService.setAccessTokenCookie(response, reIssueAccessToken);
                    jwtService.setRefreshTokenCookie(response, reIssueRefreshToken);

                    filterChain.doFilter(request, response);
                    return;
                } else {
                    // error handling
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED);
                    response.getWriter().write(new Gson().toJson(errorResponse));
                    return;
                }
            } else {
                log.debug("redirect login");
//                // 로그인 종료
//                CookieUtil.deleteCookie(request, response, jwtService.getRefreshHeader());
//                // redirect login
                response.sendRedirect("/login"); // TODO 수정하기
            }
            filterChain.doFilter(request, response);
        }
    }
}
