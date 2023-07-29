package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.PostRequest;
import com.mamoori.mamooriback.api.dto.PostResponse;
import com.mamoori.mamooriback.api.entity.Post;
import com.mamoori.mamooriback.api.repository.PostCategoryRepository;
import com.mamoori.mamooriback.api.repository.PostRepository;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.api.service.PostService;
import com.mamoori.mamooriback.api.entity.PostCategory;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final UserRepository userRepository;

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
    public Page<PostResponse> getPostList(Map<String, Object> filter, Pageable pageable) {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createAt").descending());
        Page<Post> list = postRepository.findAll(where(searchPost(filter)), sortPageable);
        Page<PostResponse> postList = list.map(m ->
                PostResponse.builder()
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
    public PostResponse getPostById(Long postId) throws Exception {
        Map<String, Object> filter = new HashMap<>();
        filter.put("postId", postId);
        Post post = postRepository.findOne(where(searchPost(filter)))
                .orElseThrow(() -> new Exception("The post doesn't exist."));

        return PostResponse.builder()
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

    @Override
    @Transactional
    public Long savePost(String email, PostRequest postRequest) {
        PostCategory findCategory = postCategoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("The category doesn't exist."));
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("The user doesn't exist."));
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(findCategory)
                .user(findUser)
                .build();
        return postRepository.save(post).getPostId();
    }



}
