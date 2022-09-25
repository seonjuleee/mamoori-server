package com.mamoori.mamooriback.service.impl;

import com.mamoori.mamooriback.entity.Post;
import com.mamoori.mamooriback.repository.PostRepository;
import com.mamoori.mamooriback.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    case "id":
                        predicates.add(builder.equal(root.get("id").as(Long.class), value));
                        break;
                    case "title":
                        predicates.add(builder.like(root.get("title").as(String.class), likeValue));
                        break;
                    case "receiver":
                        predicates.add(builder.like(root.get("receiver").as(String.class), likeValue));
                        break;
                    case "createdAt":
                        predicates.add(builder.like(root.get("createdAt").as(String.class), likeValue));
                        break;
                }
            });
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Page<Post> getPostList(Map<String, Object> filter, Pageable pageable) {
        return postRepository.findAll(
            where(searchPost(filter)), pageable
        );
    }
}
