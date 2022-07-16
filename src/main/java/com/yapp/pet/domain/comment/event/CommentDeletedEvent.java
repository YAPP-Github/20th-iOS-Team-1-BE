package com.yapp.pet.domain.comment.event;

import com.yapp.pet.domain.comment.Comment;
import com.yapp.pet.domain.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedEvent {

    private EventType type;

    private Comment comment;

    public static CommentDeletedEvent of(EventType type, Comment comment){
        return new CommentDeletedEvent(type, comment);
    }

}
