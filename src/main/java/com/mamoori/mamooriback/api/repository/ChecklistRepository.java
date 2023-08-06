package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, Long>, ChecklistRepositoryCustom {
}
