package com.mamoori.mamooriback.dto;

import com.mamoori.mamooriback.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String receiver;
    private Long views;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.receiver = post.getReceiver();
        this.views = post.getViews();
    }
}
