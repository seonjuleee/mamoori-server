package com.mamoori.mamooriback.controller.request;

import com.mamoori.mamooriback.oauth.Role;

public class UserRequest {

	private Long userId;
	private Long socialId;
	private String email;
	private String nickName;
	private String picture;
	private Role role;

	public UserRequest(){

	}

	public UserRequest(Long userId, Long socialId, String email,String nickName,  String picture
		, Role role) {
		this.userId = userId;
		this.socialId = socialId;
		this.email = email;
		this.nickName = nickName;
		this.picture = picture;
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSocialId() {
		return socialId;
	}

	public void setSocialId(Long socialId) {
		this.socialId = socialId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
