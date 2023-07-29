package com.mamoori.mamooriback.api.service;

import com.mamoori.mamooriback.api.dto.PostRequest;
import com.mamoori.mamooriback.api.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostService {
    Page<PostResponse> getPostList(Map<String, Object> filter, Pageable pageable);
    PostResponse getPostById(Long postId) throws Exception;
    Long savePost(String email, PostRequest postRequest);

}
