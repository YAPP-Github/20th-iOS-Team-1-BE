package com.yapp.pet.web.comment;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.comment.CommentService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.comment.model.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public long create(@AuthAccount Account account, @ModelAttribute CommentRequest commentRequest) {
        return commentService.addComment(account, commentRequest);
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> delete(@AuthAccount Account account, @PathVariable("comment-id") long commentId) {

        commentService.deleteComment(account, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
