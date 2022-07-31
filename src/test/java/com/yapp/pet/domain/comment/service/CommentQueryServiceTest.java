package com.yapp.pet.domain.comment.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.comment.Comment;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.web.comment.model.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CommentQueryService Unit Test")
class CommentQueryServiceTest {

    CommentRepository commentRepository;

    PetRepository petRepository;

    CommentQueryService commentQueryService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        petRepository = mock(PetRepository.class);

        commentQueryService = new CommentQueryService(commentRepository, petRepository);
    }

    @Test
    @DisplayName("findComment() : 댓글 조회 시 Account가 Pet이 없을 경우에는 Pet 정보를 넣지 않는다")
    void testFindCommentPetNull() throws Exception {
        //given
        AccountImage accountImage = AccountImage.builder()
                                                .path("path")
                                                .build();

        Account account = Account.builder()
                                 .id(1L)
                                 .nickname("account1")
                                 .accountImage(accountImage)
                                 .build();

        Club club = Club.builder()
                        .title("title1")
                        .build();

        List<Comment> savedComments = List.of(Comment.of("content", account, club));

        List<Pet> pets = new ArrayList<>();

        //when
        when(commentRepository.findCommentByClubId(anyLong()))
                .thenReturn(savedComments);
        when(petRepository.findPetsByAccountId(anyLong()))
                .thenReturn(pets);

        List<CommentResponse> comments = commentQueryService.findComment(1L, account);

        verify(commentRepository, times(1)).findCommentByClubId(anyLong());
        verify(petRepository, times(1)).findPetsByAccountId(any());

        //then
        assertAll(
                () -> assertEquals(comments.size(), 1),
                () -> assertEquals(comments.get(0).getBreeds().size(), 0)
        );
    }

    @Test
    @DisplayName("findComment() : 댓글 조회 시 Account의 Image가 없을 경우에는 accountImage를 null로 한다")
    void testFindCommentImageNull() throws Exception {
        //given
        Account account = Account.builder()
                                 .id(1L)
                                 .nickname("account1")
                                 .build();

        Club club = Club.builder()
                        .title("title1")
                        .build();

        List<Comment> savedComments = List.of(Comment.of("content", account, club));

        List<Pet> pets = List.of(Pet.builder()
                                    .name("pet1")
                                    .build());

        //when
        when(commentRepository.findCommentByClubId(anyLong()))
                .thenReturn(savedComments);
        when(petRepository.findPetsByAccountId(anyLong()))
                .thenReturn(pets);

        List<CommentResponse> comments = commentQueryService.findComment(1L, account);

        //then
        assertAll(
                () -> assertEquals(comments.size(), 1),
                () -> assertThat(comments.get(0).getImageUrl()).isNull());
    }
}