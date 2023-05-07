package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
