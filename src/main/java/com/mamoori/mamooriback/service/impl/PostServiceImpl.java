package com.mamoori.mamooriback.service.impl;

import com.mamoori.mamooriback.dto.PostResDto;
import com.mamoori.mamooriback.entity.Post;
import com.mamoori.mamooriback.repository.PostRepository;
import com.mamoori.mamooriback.service.PostService;
import com.mamoori.mamooriback.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private static Specification<Post> searchPost(Map<String, Object> filter) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            filter.forEach((key, value) -> {
                String likeValue = "%" + value + "%";
                switch (key) {
                    case "postId":
                        predicates.add(builder.equal(root.get("postId").as(Long.class), value));
                        break;
                    case "title":
                        predicates.add(builder.like(root.get("title").as(String.class), likeValue));
                        break;
                    case "createAt":
                        predicates.add(builder.greaterThanOrEqualTo(root.get("createAt").as(LocalDateTime.class), StringUtil.strToDate(value.toString(), "yyyy-MM-dd'T'HH:mm:ss")));
                        break;
                    case "categoryId":
                        predicates.add(builder.equal(root.get("category").get("categoryId").as(Long.class), value));
                        break;
                    case "userId":
                        predicates.add(builder.equal(root.get("user").get("userId").as(Long.class), value));
                        break;
                }
            });
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Page<PostResDto> getPostList(Map<String, Object> filter, Pageable pageable) {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createAt").descending());
        Page<Post> list = postRepository.findAll(where(searchPost(filter)), sortPageable);
        Page<PostResDto> postList = list.map(m ->
                PostResDto.builder()
                .postId(m.getPostId())
                .title(m.getTitle())
                .content(m.getContent())
                .receiver(m.getReceiver())
                .createAt(m.getCreateAt().toString())
                .updateAt(m.getUpdateAt().toString())
                .views(m.getViews())
                .userId(m.getUser().getUserId())
                .categoryId(m.getCategory().getCategoryId())
                .build());
        return postList;
    }

    @Override
    public PostResDto getPostById(Long postId) throws Exception {
        Map<String, Object> filter = new HashMap<>();
        filter.put("postId", postId);
        Post post = postRepository.findOne(where(searchPost(filter)))
                .orElseThrow(() -> new Exception("The post doesn't exist."));

        return PostResDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .receiver(post.getReceiver())
                .createAt(post.getCreateAt().toString())
                .updateAt(post.getUpdateAt().toString())
                .views(post.getViews())
                .userId(post.getUser().getUserId())
                .categoryId(post.getCategory().getCategoryId())
                .build();
    }
}
