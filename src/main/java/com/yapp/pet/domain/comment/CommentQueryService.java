package com.yapp.pet.domain.comment;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.web.comment.model.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(NoSuchElementException::new);
    }

    public List<CommentResponse> findComment(long clubId, Account account) {
        List<Comment> comments = commentRepository.findCommentByClubId(clubId);

        return comments.stream()
                       .map(comment -> new CommentResponse(comment.getContent(),
                                                           comment.getAccount().getNickname(),
                                                           comment.getClub().getAccountClubs().stream()
                                                                  .anyMatch(ac -> (ac.isLeader() && ac.getAccount()
                                                                                                      .equals(account))),
                                                           comment.getUpdatedAt()
                                                                  .atZone(ZoneId.of("Asia/Seoul"))))
                       .collect(Collectors.toList());
    }
}
