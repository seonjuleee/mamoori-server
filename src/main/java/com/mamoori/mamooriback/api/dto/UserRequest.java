package com.mamoori.mamooriback.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRequest {
	private Long userId;
	private String email;
	private String name;
}
