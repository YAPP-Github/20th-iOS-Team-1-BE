package com.yapp.pet.web.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private String author;

    private boolean leader;

    private ZonedDateTime updatedTime;

    private List<String> breeds;

    private String imageUrl;

    @Builder
    public CommentResponse(Long id, String content, String author, boolean leader, ZonedDateTime updatedTime,
                           List<String> breeds, String imageUrl) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.leader = leader;
        this.updatedTime = updatedTime;
        this.breeds = breeds;
        this.imageUrl = imageUrl;
    }
}
