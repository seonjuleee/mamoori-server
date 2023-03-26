/*
package com.mamoori.mamooriback.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mamoori.mamooriback.oauth.service.OAuthService;
import com.mamoori.mamooriback.oauth.AuthToken;
import com.mamoori.mamooriback.oauth.AuthTokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuthController {
	@Autowired
	private OAuthService oAuthService;
	private AuthTokenProvider jwtManager;

	@RequestMapping("/oauth2/users/kakao")
	public AuthToken kakaoLogin(@RequestParam String code) throws IOException {
		String accessToken = oAuthService.getKakaoAccessToken(code);
		System.out.println("accessToken : " +accessToken);
		AuthToken jwtToken = oAuthService.getKakaoUserInfo(accessToken);
		System.out.println("jwtToken : " + jwtToken.getToken());
		return jwtToken;
	}
}
*/
