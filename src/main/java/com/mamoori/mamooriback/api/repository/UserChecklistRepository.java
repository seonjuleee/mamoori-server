package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.entity.UserChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserChecklistRepository extends JpaRepository<UserChecklist, Long>, UserChecklistRepositoryCustom {
    Optional<UserChecklist> findByUser_EmailAndUserChecklistId(String email, Long userChecklistId);
    Optional<UserChecklist> findById(Long userChecklistId);

    @Query(value = "select " +
            "user_checklist.user_checklist_id AS id, " +
            "rel.prevId AS prevId, " +
            "rel.nextId AS nextId" +
            " from user_checklist" +
            " join" +
            "(select user_checklist_id," +
        "        LAG(user_checklist_id, 1, null) OVER (ORDER BY created_at DESC)  AS prevId," +
        "        LEAD(user_checklist_id, 1, null) OVER (ORDER BY created_at DESC) AS nextId" +
        " from user_checklist" +
        " where user_id = :userId" +
        " order by created_at desc) rel " +
            " on rel.user_checklist_id = user_checklist.user_checklist_id" +
            " where rel.user_checklist_id = :userChecklistId"
            , nativeQuery = true)
    Optional<ChecklistPrevAndNext> findPrevAndNextById(@Param("userId") Long userId, @Param("userChecklistId") Long userChecklistId);

    interface ChecklistPrevAndNext {
        Long getId();
        Long getPrevId();
        Long getNextId();
    }
}
