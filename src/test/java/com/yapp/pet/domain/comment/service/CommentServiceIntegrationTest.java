package com.yapp.pet.domain.comment.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.support.AbstractIntegrationTest;
import com.yapp.pet.web.comment.model.CommentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @DisplayName("addComment() : 유저 정보, Club Id, Comment 내용을 정상적으로 요청할 경우 Comment가 생성된다")
    void testAddComment() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();
        CommentRequest commentRequest = new CommentRequest(1L, "comment1");

        int beforeSaveCommentSize = commentRepository.findAll().size();

        //when
        commentService.addComment(account, commentRequest);

        //then
        assertEquals(commentRepository.findAll().size(), beforeSaveCommentSize + 1);
    }

    @Test
    @DisplayName("deleteComment() : 유저 정보, 댓글 id를 정상적으로 요청하고, 해당 유저의 댓글일 경우 삭제할 수 있다")
    void testDeleteComment() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();
        long commentId = 1L;

        //when
        Long deletedCommentId = commentService.deleteComment(account, commentId);

        //then
        assertEquals(deletedCommentId, commentId);
    }
}
