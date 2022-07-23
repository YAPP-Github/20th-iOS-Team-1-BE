package com.yapp.pet.domain.comment.event;

import com.yapp.pet.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedEvent {

    private Comment comment;

    public static CommentDeletedEvent from(Comment comment){
        return new CommentDeletedEvent(comment);
    }

}
