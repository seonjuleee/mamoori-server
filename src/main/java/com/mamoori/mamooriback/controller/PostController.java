package com.mamoori.mamooriback.controller;

import com.mamoori.mamooriback.controller.request.PostRequest;
//import com.mamoori.mamooriback.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

//    private final PostService postService;

    /**
     * 커뮤니티 글 리스트 조회
     * */
    @PostMapping("/community")
    public /*ResponseEntity<PostResDto>*/ String getPostList(@RequestBody PostRequest postRequest
                                                  /*@PageableDefault(size=10, sort="createdAt", direction = Sort.Direction.DESC)*/ /*Pageable pageable*/) {
        log.debug("getPostList -> postRequest : {}", postRequest);

        return "OK";

        /*PostResDto data = PostResDto.builder()
                .test("test")
                .build();
        return ResponseEntity.ok()
                .body(data);*/
    }

}
