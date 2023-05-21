package com.mamoori.mamooriback.api.service;

import java.util.Optional;

import com.mamoori.mamooriback.api.entity.User;

public interface UserService {
	Optional<User> findByEmail(String email);
	User create(User user);
}
