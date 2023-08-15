package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.QWillResponse;
import com.mamoori.mamooriback.api.dto.WillPageResponse;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.mamoori.mamooriback.api.entity.QUser.user;
import static com.mamoori.mamooriback.api.entity.QWill.will;

public class WillRepositoryImpl implements WillRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WillRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public WillPageResponse search(String email, String title, Pageable pageable) {
        List<WillResponse> content = queryFactory
                .select(new QWillResponse(will.willId,
                        will.title,
                        will.content,
                        will.createAt,
                        will.updateAt))
                .from(will)
                .join(will.user, user)
                .where(user.email.eq(email),
                        titleContains(title))
                .orderBy(will.updateAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return WillPageResponse.builder()
                .wills(content)
                .totalWillCount(getCount(email, title))
                .size(pageable.getPageSize())
                .page(pageable.getPageNumber())
                .latestWillDate(findLatestWillByEmail(email))
                .build();
    }

    private LocalDateTime findLatestWillByEmail(String email) {
        return queryFactory
                .select(will.createAt)
                .from(will)
                .where(will.user.email.eq(email))
                .orderBy(will.createAt.desc())
                .limit(1)
                .fetchOne();
    }

    private Long getCount(String email, String title) {
        return queryFactory
                .select(will.count())
                .from(will)
                .join(will.user, user)
                .where(user.email.eq(email),
                        titleContains(title))
                .fetchOne();
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? will.title.contains(title) : null;
    }
}
