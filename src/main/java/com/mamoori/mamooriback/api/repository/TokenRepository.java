package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.Token;
import com.mamoori.mamooriback.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
}
