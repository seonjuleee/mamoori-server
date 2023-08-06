package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistResponse;
import com.mamoori.mamooriback.api.dto.QChecklistResponse;
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
    public List<ChecklistResponse> getChecklistItems() {
        return queryFactory
                .select(new QChecklistResponse(
                        checklist.checklistId,
                        checklist.description,
                        checklist.order
                ))
                .from(checklist)
                .orderBy(checklist.order.asc())
                .fetch();
    }

}
