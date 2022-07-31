package com.yapp.pet.domain.comment.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.web.comment.model.CommentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CommentService Unit Test")
class CommentServiceTest {

    CommentRepository commentRepository;

    ClubRepository clubRepository;

    CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        clubRepository = mock(ClubRepository.class);

        commentService = new CommentService(commentRepository, clubRepository);
    }

    @Test
    @DisplayName("addComment() : 유저 정보와 댓글을 입력할 Club id, 댓글 내용을 입력할 경우 댓글을 저장할 수 있다")
    void testAddComment() throws Exception {
        //given
        Account savedAccount = Account.builder()
                                  .id(1L)
                                  .nickname("account1")
                                  .build();

        Club savedClub = Club.builder()
                             .title("club1")
                             .build();

        CommentRequest commentRequest = new CommentRequest(1L, "content1");

        //when
        when(clubRepository.findByIdWrapper(anyLong()))
                .thenReturn(savedClub);

        commentService.addComment(savedAccount, commentRequest);

        //then
        verify(commentRepository, times(1)).save(any());
    }
}