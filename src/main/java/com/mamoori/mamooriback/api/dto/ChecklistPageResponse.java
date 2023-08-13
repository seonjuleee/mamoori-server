package com.mamoori.mamooriback.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ChecklistPageResponse {
    private Long totalChecklistCount;
    private Integer page;
    private Integer size;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime latestChecklistDate;
    private List<ChecklistResponse> checklists;

    public ChecklistPageResponse(Long totalChecklistCount, Integer page, Integer size, List<ChecklistResponse> checklists, LocalDateTime latestChecklistDate) {
        this.totalChecklistCount = totalChecklistCount;
        this.page = page;
        this.size = size;
        this.checklists = checklists;
        this.latestChecklistDate = latestChecklistDate;
    }
}
