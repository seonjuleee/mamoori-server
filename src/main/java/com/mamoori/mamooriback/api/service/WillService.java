package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.PageResponse;
import com.mamoori.mamooriback.api.dto.WillResponse;
import org.springframework.data.domain.Pageable;


public interface WillService {
    PageResponse<WillResponse> getWillListByEmail(String email, String title, Pageable pageable);
    WillResponse getWillById(String email, Long id);
}
