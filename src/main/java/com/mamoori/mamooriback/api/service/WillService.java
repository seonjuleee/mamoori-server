package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;


public interface WillService {
    WillResponse getWillByEmail(String email);
    void putWill(String email, WillRequest willRequest);
    void deleteWill(String email);
}
