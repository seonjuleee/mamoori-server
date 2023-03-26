package com.mamoori.mamooriback.api.dto;

import com.mamoori.mamooriback.api.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private String receiver;
    private Long views;

    public PostDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.receiver = post.getReceiver();
        this.views = post.getViews();
    }
}
