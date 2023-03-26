package com.mamoori.mamooriback.service;

import java.util.Optional;

import com.mamoori.mamooriback.entity.User;

public interface UserService {
	Optional<User> findByEmail(String email);
	User create(User user);
}
