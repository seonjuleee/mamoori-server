package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistResponse;

import java.util.List;

public interface ChecklistRepositoryCustom {
    List<ChecklistResponse> getChecklistItems();
}
