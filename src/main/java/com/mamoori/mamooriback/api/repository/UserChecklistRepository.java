package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChecklistRepository extends JpaRepository<UserChecklist, Long>, UserChecklistRepositoryCustom {
}
