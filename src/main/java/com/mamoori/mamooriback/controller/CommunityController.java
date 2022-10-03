package com.mamoori.mamooriback.controller;

import com.mamoori.mamooriback.controller.request.PostRequest;
import com.mamoori.mamooriback.dto.PostResDto;
import com.mamoori.mamooriback.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/community")
public class CommunityController {
    private final PostService postService;

    /**
     * 커뮤니티 글 리스트 조회
     * */
    @PostMapping("/post")
    public ResponseEntity<List<PostResDto>> getPostList(@RequestBody PostRequest postRequest,
                                                        @PageableDefault(size=10, sort="createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("getPostList -> postRequest : {}", postRequest);
//        log.debug("getPostList -> number : {}, offset : {}", pageable.getPageNumber(), pageable.getOffset());

        Map<String, Object> filter = new HashMap<>();
        filter.put("title", postRequest.getTitle());

        Page<PostResDto> list = postService.getPostList(filter, pageable);

        return ResponseEntity.ok()
                .body(list.getContent());
    }

}
