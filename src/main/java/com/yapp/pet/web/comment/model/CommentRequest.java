package com.yapp.pet.web.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    @NotNull
    private Long clubId;

    @NotBlank
    private String content;
}
