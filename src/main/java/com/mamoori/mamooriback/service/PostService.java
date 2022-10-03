package com.mamoori.mamooriback.service;

import com.mamoori.mamooriback.dto.PostResDto;
import com.mamoori.mamooriback.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostService {
    Page<PostResDto> getPostList(Map<String, Object> filter, Pageable pageable);
}
