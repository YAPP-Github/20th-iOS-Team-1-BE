package com.yapp.pet.web.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private String author;

    private boolean leader;

    private ZonedDateTime updatedTime;

    private List<String> breeds;

    private String imageUrl;
}
