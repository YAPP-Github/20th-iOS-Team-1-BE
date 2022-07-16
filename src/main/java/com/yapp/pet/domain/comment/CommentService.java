package com.yapp.pet.domain.comment;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.service.ClubQueryService;
import com.yapp.pet.web.comment.model.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ClubQueryService clubQueryService;
    private final CommentQueryService commentQueryService;

    public Long addComment(Account account, CommentRequest commentRequest) {

        Comment comment = Comment.of(commentRequest.getContent(), account,
                                clubQueryService.findClubById(commentRequest.getClubId()));

        commentRepository.save(comment);

        return comment.getId();
    }

    public Long deleteComment(Account account, long commentId) {
        Comment savedComment = commentQueryService.findCommentById(commentId);

        if (!account.isMe(savedComment.getAccount())) {
            throw new IllegalArgumentException("당사자만 지울 수 있다");
        }

        commentRepository.delete(savedComment);

        return savedComment.getId();
    }

    public void deleteAllComment(Account account) {
        commentRepository.deleteCommentByAccountId(account.getId());
    }
}