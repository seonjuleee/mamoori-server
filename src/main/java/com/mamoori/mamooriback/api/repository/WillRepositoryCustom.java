package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.WillPageResponse;
import org.springframework.data.domain.Pageable;

public interface WillRepositoryCustom {
    WillPageResponse search(String email, String title, Pageable pageable);
}

