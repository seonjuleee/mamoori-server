package com.mamoori.mamooriback.auth.dto;

import java.util.Map;

public class NaverOauthUserInfo implements OauthUserInfo {
    private Map<String, Object> attributes;
    private final String PROVIDER = "naver";

    public NaverOauthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = getResponse();
        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }


    public String getEmail() {
        Map<String, Object> response = getResponse();
        if (response == null) {
            return null;
        }
        return (String) response.get("email");
    }

    public String getName() {
        Map<String, Object> response = getResponse();
        if (response == null) {
            return null;
        }
        return (String) response.get("name");
    }

    private Map<String, Object> getResponse() {
        return (Map<String, Object>) attributes.get("response");
    }
}
