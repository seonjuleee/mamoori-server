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
    @GetMapping("/posts")
    public ResponseEntity<List<PostResDto>> getPostList(
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

        Page<PostResDto> list = postService.getPostList(filter, pageable);

        return ResponseEntity.ok()
                .body(list.getContent());
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResDto> getPost(
            @PathVariable(name = "postId") Long postId) throws Exception {
        PostResDto postResDto = postService.getPostById(postId);

        return ResponseEntity.ok()
                .body(postResDto);

    }

}
