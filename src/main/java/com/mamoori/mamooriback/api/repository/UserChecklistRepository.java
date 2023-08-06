package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChecklistRepository extends JpaRepository<UserChecklist, Long>, UserChecklistRepositoryCustom {
    Optional<UserChecklist> findByUser_EmailAndUserChecklistId(String email, Long userChecklistId);
}
