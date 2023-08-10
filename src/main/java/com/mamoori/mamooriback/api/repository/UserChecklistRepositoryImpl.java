package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import java.util.List;

import static com.mamoori.mamooriback.api.entity.QUserChecklist.userChecklist;
import static com.mamoori.mamooriback.api.entity.QUserChecklistAnswer.userChecklistAnswer;

@Slf4j
public class UserChecklistRepositoryImpl implements UserChecklistRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserChecklistRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ChecklistAnswerResponse findLastChecklistAnswerByEmail(String email) {
        return queryFactory
                .select(new QChecklistAnswerResponse(
                        userChecklist.userChecklistId,
                        userChecklist.createAt
                ))
                .from(userChecklist)
                .where(userChecklist.user.email.eq(email))
                .orderBy(userChecklist.createAt.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<UserChecklistAnswerResponse> findUserChecklistAnswersByUserChecklistId(Long userChecklistId) {
        return queryFactory
                .select(new QUserChecklistAnswerResponse(userChecklistAnswer.checklist.checklistId, userChecklistAnswer.isCheck))
                .from(userChecklistAnswer)
                .where(userChecklistAnswer.userChecklist.userChecklistId.eq(userChecklistId))
                .fetch();
    }
}
