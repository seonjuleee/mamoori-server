package com.mamoori.mamooriback.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> content;
    private int totalSize;
    private int pageSize;
    private int pageNumber;

    public PageResponse(List<T> content, int totalSize, int pageSize, int pageNumber) {
        this.content = content;
        this.totalSize = totalSize;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
