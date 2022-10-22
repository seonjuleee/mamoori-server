package com.mamoori.mamooriback.repository;

import com.mamoori.mamooriback.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long>, JpaSpecificationExecutor<PostCategory> {
}
