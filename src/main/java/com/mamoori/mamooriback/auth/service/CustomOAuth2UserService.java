package com.mamoori.mamooriback.auth.service;

import com.mamoori.mamooriback.api.entity.Role;
import com.mamoori.mamooriback.auth.dto.GoogleOauthUserInfo;
import com.mamoori.mamooriback.auth.dto.NaverOauthUserInfo;
import com.mamoori.mamooriback.auth.dto.OauthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final String NAVER = "naver";
    private static final String GOOGLE = "google";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("userRequest -> client registration : {}", userRequest.getClientRegistration());
        log.debug("userRequest -> token : {}", userRequest.getAccessToken().getTokenValue());
        log.debug("userRequest -> attributes : {}", super.loadUser(userRequest).getAttributes());

        ClientAuthenticationMethod clientAuthenticationMethod = userRequest.getClientRegistration().getClientAuthenticationMethod();

        Set<String> strings = userRequest.getAdditionalParameters().keySet();
        for (String s : strings) {
            log.debug("userRequest -> s : {}, v : {}", s, userRequest.getAdditionalParameters().get(s));
        }

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OauthUserInfo oauthUserInfo = getOauthUserInfoByRegistrationId(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        log.debug("oauthUserInfo.getProvider() : {} ", oauthUserInfo.getProvider());
        log.debug("oauthUserInfo.getProvider() : {} ", oauthUserInfo.getProviderId());
        log.debug("oauthUserInfo.getProvider() : {} ", oauthUserInfo.getEmail());
        log.debug("oauthUserInfo.getProvider() : {} ", oauthUserInfo.getName());

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.USER.getCode())),
                oAuth2User.getAttributes(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(),
                oauthUserInfo.getEmail(),
                oauthUserInfo.getName(),
                oauthUserInfo.getProvider(),
                Role.USER
        );
    }

    private OauthUserInfo getOauthUserInfoByRegistrationId(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals(NAVER)) {
            return new NaverOauthUserInfo(attributes);
        }
        if (registrationId.equals(GOOGLE)) {
            return new GoogleOauthUserInfo(attributes);
        }
        return null;
    }

}
