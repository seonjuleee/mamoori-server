package com.mamoori.mamooriback.api.repository;

import com.mamoori.mamooriback.api.dto.ChecklistTaskResponse;

import java.util.List;

public interface ChecklistRepositoryCustom {
    List<ChecklistTaskResponse> getChecklistTasks();
}
