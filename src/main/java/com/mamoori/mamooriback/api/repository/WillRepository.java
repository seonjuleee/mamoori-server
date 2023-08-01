package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.Will;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WillRepository extends JpaRepository<Will, Long>, WillRepositoryCustom {
}
