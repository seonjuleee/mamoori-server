package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.WillPageResponse;
import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;
import org.springframework.data.domain.Pageable;


public interface WillService {
    WillPageResponse getWillListByEmail(String email, String title, Pageable pageable);
    WillResponse getWillByEmail(String email);
    void putWill(String email, WillRequest willRequest);
    void deleteWill(String email, Long willId);
}
