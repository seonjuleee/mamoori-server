package com.mamoori.mamooriback.api.repository;

import java.util.Optional;

import com.mamoori.mamooriback.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}