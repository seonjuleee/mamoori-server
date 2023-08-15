package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistTaskResponse;
import com.mamoori.mamooriback.api.dto.QChecklistTaskResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mamoori.mamooriback.api.entity.QChecklist.checklist;

public class ChecklistRepositoryImpl implements ChecklistRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ChecklistRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChecklistTaskResponse> getChecklistTasks() {
        return queryFactory
                .select(new QChecklistTaskResponse(
                        checklist.checklistId,
                        checklist.description,
                        checklist.order
                ))
                .from(checklist)
                .orderBy(checklist.order.asc())
                .fetch();
    }

}
