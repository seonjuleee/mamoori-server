package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.WillResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface WillRepositoryCustom {
    Page<WillResponse> search(String email, String title, Pageable pageable);
}

