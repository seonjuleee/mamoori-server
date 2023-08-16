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
    private Integer pageNumber;
    private Integer size;
    private Integer totalPages;
    private boolean first;
    private boolean last;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime latestChecklistDate;
    private List<ChecklistResponse> checklists;

    public ChecklistPageResponse(Long totalChecklistCount,
                                 Integer pageNumber,
                                 Integer size,
                                 Integer totalPages,
                                 boolean first,
                                 boolean last,
                                 LocalDateTime latestChecklistDate,
                                 List<ChecklistResponse> checklists) {
        this.totalChecklistCount = totalChecklistCount;
        this.pageNumber = pageNumber;
        this.size = size;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
        this.latestChecklistDate = latestChecklistDate;
        this.checklists = checklists;
    }
}
