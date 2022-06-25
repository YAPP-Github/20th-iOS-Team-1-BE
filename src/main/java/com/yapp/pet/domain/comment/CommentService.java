package com.yapp.pet.domain.comment;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.ClubQueryService;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubRepository;
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

    public long addComment(Account account, CommentRequest commentRequest) {

        Comment comment = Comment.of(commentRequest.getContent(), account,
                                clubQueryService.findClubById(commentRequest.getClubId()));

        commentRepository.save(comment);

        return comment.getId();
    }
}
