package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.Will;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WillRepository extends JpaRepository<Will, Long>, WillRepositoryCustom {
    Optional<Will> findByWillId(Long id);
    Optional<Will> findByUser_Email(String email);
}
