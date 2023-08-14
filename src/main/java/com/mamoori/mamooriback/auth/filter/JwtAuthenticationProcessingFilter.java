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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final List<String> NO_CHECK_URL = Arrays.asList(new String[] {
            "/login", "/checklist/items", "/callback", "/api/token"
    }); // "/login"으로 들어오는 요청은 Filter 작동 X

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
        log.debug("doFilterInternal called...");
        log.debug("method : {}", request.getMethod());
        log.debug("uri : {}", request.getRequestURI());
        log.debug("host : {}", request.getRemoteHost());
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (NO_CHECK_URL.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        Optional<String> optionalAccessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid);
        if (optionalAccessToken.isPresent()) {
            // API 처리
            jwtService.extractEmailByAccessToken(optionalAccessToken.get())
                    .ifPresent(email -> userRepository.findByEmail(email));
            filterChain.doFilter(request, response);
            return;
        } else {
            // error handling
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_ACCESS_TOKEN);
            response.getWriter().write(new Gson().toJson(errorResponse));
            return;
        }
    }
}
