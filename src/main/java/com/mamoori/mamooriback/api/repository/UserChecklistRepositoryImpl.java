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
}
