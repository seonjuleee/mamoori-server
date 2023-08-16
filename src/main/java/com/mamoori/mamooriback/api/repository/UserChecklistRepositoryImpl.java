package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.*;
import com.mamoori.mamooriback.api.entity.UserChecklist;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
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
    public List<ChecklistDto> getChecklist(Long userChecklistId) {
        return queryFactory
                .select(new QChecklistDto(userChecklistAnswer.checklist.checklistId,
                        userChecklistAnswer.isCheck,
                        userChecklistAnswer.checklist.description))
                .from(userChecklist)
                .join(userChecklist.userChecklistAnswers, userChecklistAnswer)
                .where(userChecklist.userChecklistId.eq(userChecklistId))
                .orderBy(userChecklistAnswer.checklist.order.asc())
                .fetch();
    }

    @Override
    public Long getTotalTaskCount(Long userChecklistId) {
        return queryFactory
                .select(userChecklistAnswer.answerId.count())
                .from(userChecklist)
                .join(userChecklist.userChecklistAnswers, userChecklistAnswer)
                .where(userChecklist.userChecklistId.eq(userChecklistId))
                .fetchOne();
    }

    @Override
    public Long getCheckedTaskCount(Long userChecklistId) {
        return queryFactory
                .select(userChecklistAnswer.answerId.count())
                .from(userChecklist)
                .join(userChecklist.userChecklistAnswers, userChecklistAnswer)
                .where(userChecklist.userChecklistId.eq(userChecklistId)
                        .and(userChecklistAnswer.isCheck.eq(true)))
                .fetchOne();
    }

    @Override
    public Page<UserChecklist> getChecklistPage(String email, Pageable pageable) {
        List<UserChecklist> result = queryFactory
                .select(userChecklist)
                .from(userChecklist)
                .where(userChecklist.user.email.eq(email))
                .orderBy(userChecklist.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, getCount(email));
    }

    public Long getCount(String email) {
        return queryFactory
                .select(userChecklist.count())
                .from(userChecklist)
                .where(userChecklist.user.email.eq(email))
                .fetchOne();
    }

    @Override
    public LocalDateTime findLastChecklistAnswerByEmail(String email) {
        return queryFactory
                .select(userChecklist.createAt)
                .from(userChecklist)
                .where(userChecklist.user.email.eq(email))
                .orderBy(userChecklist.createAt.desc())
                .limit(1)
                .fetchOne();
    }

}
