package com.mamoori.mamooriback.controller;

import com.mamoori.mamooriback.controller.request.PostRequest;
import com.mamoori.mamooriback.dto.PostResponse;
import com.mamoori.mamooriback.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    // TODO result 형태 변경하기(상태코드 포함)
    /**
     * 커뮤니티 글 리스트 조회
     * */
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getPostList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String createAt,
            @PageableDefault(size=10, sort="createAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Map<String, Object> filter = new HashMap<>();
        if (title != null) {
            filter.put("title", title);
        }
        if (categoryId != null) {
            filter.put("categoryId", categoryId);
        }
        if (userId != null) {
            filter.put("userId", userId);
        }
        if (createAt != null) {
            filter.put("createAt", createAt);
        }

        Page<PostResponse> list = postService.getPostList(filter, pageable);

        return ResponseEntity.ok()
                .body(list.getContent());
    }

    /**
     * 글 id로 커뮤니티 글 조회
     * */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable(name = "postId") Long postId) throws Exception {
        PostResponse postResponse = postService.getPostById(postId);

        return ResponseEntity.ok()
                .body(postResponse);

    }

    }

}
