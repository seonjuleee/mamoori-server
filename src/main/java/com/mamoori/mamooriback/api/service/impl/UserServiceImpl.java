package com.mamoori.mamooriback.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mamoori.mamooriback.api.controller.request.UserRequest;
import com.mamoori.mamooriback.api.service.UserService;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private final List<User> users = new ArrayList<>();

	// create 없이 delete update read 만 있어도 됨
	public User create(UserRequest userParameter) {
		final User user = new User(userParameter.getUserId(),userParameter.getSocialId(), userParameter.getEmail(),userParameter.getNickName(),userParameter.getPicture(),userParameter.getRole());
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User create(User user) {
		return userRepository.save(user);
	}

	@Cacheable(cacheNames = "user", key = "#id")
	public User read(Long id) {
		return userRepository.findById(id).orElseThrow(NullPointerException::new);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}