package com.mamoori.mamooriback.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mamoori.mamooriback.oauth.AuthTokenProvider;

@Configuration
public class JwtConfig {
	private static String jwtSecret = "dGVzdC1qd3Qtc2ewFtcGxlLXNwcmluZ2Jvb3QQQ=";

	//유효시간을 86400초로 계산
	private long tokenValidityInSeconds = 86400;

	@Bean
	public AuthTokenProvider jwtManager() {
		return new AuthTokenProvider(jwtSecret);
	}
	public static String getJwtSecret() {
		return jwtSecret;
	}
}
