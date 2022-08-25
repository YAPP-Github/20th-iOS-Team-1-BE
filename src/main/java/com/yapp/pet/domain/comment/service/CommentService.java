package com.yapp.pet.domain.comment.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.comment.Comment;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.global.exception.comment.NotDeleteCommentException;
import com.yapp.pet.web.comment.model.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ClubRepository clubRepository;

    public void addComment(Account account, CommentRequest commentRequest) {

        Comment comment = Comment.of(commentRequest.getContent(), account,
                                clubRepository.findByIdWrapper(commentRequest.getClubId()));

        commentRepository.save(comment);
    }

    public Long deleteComment(Account account, long commentId) {
        Comment savedComment = commentRepository.findById(commentId)
                                                .orElseThrow(EntityNotFoundException::new);

        if (!account.isMe(savedComment.getAccount())) {
            throw new NotDeleteCommentException();
        }

        commentRepository.delete(savedComment);

        return savedComment.getId();
    }
}