package com.mamoori.mamooriback.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class WillPageResponse {
    private Long totalWillCount;
    private Integer page;
    private Integer size;
    private List<WillResponse> wills;
}
