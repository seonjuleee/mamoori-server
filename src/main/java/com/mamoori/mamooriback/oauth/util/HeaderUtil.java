package com.mamoori.mamooriback.oauth.util;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {
	public static String getAccessToken(HttpServletRequest request){
		final String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) return null;
		final String[] value = authorizationHeader.split(" ");
		return value[1];
	}
}
