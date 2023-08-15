package com.mamoori.mamooriback.auth.handler;

import com.mamoori.mamooriback.api.entity.Role;
import com.mamoori.mamooriback.api.entity.Token;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.repository.TokenRepository;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.auth.service.CustomOAuth2User;
import com.mamoori.mamooriback.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess called...");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getEmail();
            String name = oAuth2User.getName();
            String image = oAuth2User.getProfileImage();
            Role role = oAuth2User.getRole();

            log.debug("oauth2User email : {}", oAuth2User.getEmail());
            log.debug("oauth2User image : {}", oAuth2User.getProfileImage());
            log.debug("oauth2User role : {}", oAuth2User.getRole());

            // AccessToken, RefreshToken 발급
            String accessToken = jwtService.createAccessToken(email);
            String refreshToken = jwtService.createRefreshToken();

            log.debug("accessToken : {}", accessToken);
            log.debug("refreshToken : {}", refreshToken);

            // User에 저장
            Optional<User> findUser = userRepository.findByEmail(email);
            if (!findUser.isPresent()) {
                log.debug("user invalid");
                User saveUser = userRepository.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .image(image)
                                .role(role)
                                .build()
                );
                tokenRepository.save(
                        Token.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .socialType(oAuth2User.getSocialType())
                                .user(saveUser)
                                .build()
                );
            } else {
                log.debug("user valid");
                User user = findUser.get();
                Optional<Token> findToken = tokenRepository.findByUser(user);
                if (!findToken.isPresent()) {
                    tokenRepository.save(
                            Token.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .socialType(oAuth2User.getSocialType())
                                    .user(findUser.get())
                                    .build()
                    );
                } else {
                    Token token = findToken.get();
                    token.setAccessToken(accessToken);
                    token.setRefreshToken(refreshToken);
                    tokenRepository.save(token);
                }
            }

            jwtService.setAccessTokenCookie(response, accessToken);
            jwtService.setRefreshTokenCookie(response, refreshToken);
            log.debug("redirect login...");
            // redirect login
            response.sendRedirect("https://mamoori.life/callback");
            return;
        } catch (Exception e) {
            throw e;
        }
    }
}
