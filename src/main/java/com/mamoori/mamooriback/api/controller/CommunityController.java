package com.mamoori.mamooriback.api.controller;

import com.mamoori.mamooriback.api.dto.PostResponse;
import com.mamoori.mamooriback.api.service.PostService;
import com.mamoori.mamooriback.api.dto.PostRequest;
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

    /**
     * 커뮤니티 글 추가
     * */
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) throws Exception {
        // TODO email session 이용하기
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = (User)principal;
//        String email = user.getEmail();
//        System.out.println("email = " + email);
        String email = "dtw8073297@daum.net"; // session 추가 전 test! TODO 지우기
        postService.savePost(email, postRequest);
        return (ResponseEntity<?>) ResponseEntity.ok().build();
    }

}
