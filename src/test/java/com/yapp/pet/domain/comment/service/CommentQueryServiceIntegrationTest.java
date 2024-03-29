package com.yapp.pet.domain.comment.service;


import com.yapp.pet.support.AbstractIntegrationTest;
import com.yapp.pet.web.comment.model.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CommentQueryService Integration Test")
@RequiredArgsConstructor
public class CommentQueryServiceIntegrationTest extends AbstractIntegrationTest {

    private final CommentQueryService commentQueryService;

    @Test
    @DisplayName("findComment() : club Id가 주어질 경우 해당 클럽의 모든 댓글들을 조회할 수 있다")
    void testFindComment() {
        //given
        long clubId = 1L;

        //when
        List<CommentResponse> comments = commentQueryService.findComment(clubId);

        //then
        assertEquals(comments.size(), 2);
        assertThat(comments).extracting("id")
                            .contains(1L, 2L);
    }

    @Test
    @DisplayName("findComment() : club 만든 사람이 댓글을 달 경우 leader가 true이다")
    void testFindCommentClubLeader() throws Exception {
        //given
        long clubId = 1L;

        //when
        List<CommentResponse> comments = commentQueryService.findComment(clubId);

        //then
        assertTrue(comments.get(0).isLeader());
    }
}
