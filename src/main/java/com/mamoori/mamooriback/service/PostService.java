package com.mamoori.mamooriback.service;

import com.mamoori.mamooriback.controller.request.PostRequest;
import com.mamoori.mamooriback.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostService {
    Page<PostResponse> getPostList(Map<String, Object> filter, Pageable pageable);
    PostResponse getPostById(Long postId) throws Exception;
    Long savePost(String email, PostRequest postRequest);

}
