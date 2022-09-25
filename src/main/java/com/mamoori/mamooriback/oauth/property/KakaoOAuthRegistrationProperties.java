package com.mamoori.mamooriback.oauth.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class KakaoOAuthRegistrationProperties {
    private String clientId;
    private String redirectUri;
    private String clientSecret;

}
