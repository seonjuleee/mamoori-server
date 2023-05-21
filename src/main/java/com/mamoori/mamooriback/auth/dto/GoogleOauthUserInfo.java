package com.mamoori.mamooriback.auth.dto;

import java.util.Map;

public class GoogleOauthUserInfo implements OauthUserInfo {
    private Map<String, Object> attributes;
    private final String PROVIDER = "google";

    public GoogleOauthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = getResponse();
        if (response == null) {
            return null;
        }
        return (String) attributes.get("sub");
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
        return (String) attributes.get("email");
    }

    public String getName() {
        Map<String, Object> response = getResponse();
        if (response == null) {
            return null;
        }
        return (String) attributes.get("name");
    }

    private Map<String, Object> getResponse() {
        return (Map<String, Object>) attributes.get("response");
    }
}
