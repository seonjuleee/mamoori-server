package com.mamoori.mamooriback.auth.dto;

public interface OauthUserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getProfileImage();

}