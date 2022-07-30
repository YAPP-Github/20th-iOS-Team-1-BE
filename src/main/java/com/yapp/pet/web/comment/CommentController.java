package com.yapp.pet.web.comment;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.service.ClubQueryService;
import com.yapp.pet.domain.comment.service.CommentService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.club.model.ClubFindDetailResponse;
import com.yapp.pet.web.comment.model.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    private final ClubQueryService clubQueryService;

    @PostMapping("/comments")
    public ResponseEntity<ClubFindDetailResponse> create(@AuthAccount Account account, @RequestBody CommentRequest commentRequest) {

        commentService.addComment(account, commentRequest);

        ClubFindDetailResponse clubDetailInfo = clubQueryService.findClubDetail(commentRequest.getClubId(), account);

        return ResponseEntity.ok(clubDetailInfo);
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Long> delete(@AuthAccount Account account, @PathVariable("comment-id") long commentId) {

        Long deletedCommentId = commentService.deleteComment(account, commentId);

        return ResponseEntity.ok(deletedCommentId);
    }
}
