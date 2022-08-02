package com.yapp.pet.domain.comment.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.comment.Comment;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.web.comment.model.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    private final PetRepository petRepository;

    public List<CommentResponse> findComment(long clubId) {
        List<Comment> comments = commentRepository.findCommentByClubId(clubId);

        return comments.stream()
                       .map(comment -> CommentResponse.builder()
                                                      .id(comment.getId())
                                                      .content(comment.getContent())
                                                      .author(comment.getAccount().getNickname())
                                                      .leader(isLeader(comment.getClub(), comment))
                                                      .updatedTime(comment.getUpdatedAt() != null ? ZonedDateTime.of(
                                                              comment.getUpdatedAt(), ZoneId.of("UTC")) : null)
                                                      .breeds(findPetInfoForComment(comment.getAccount().getId()))
                                                      .imageUrl(comment.getAccount().hasImage() ? comment.getAccount()
                                                                                                         .getAccountImage()
                                                                                                         .getPath() : null)
                                                      .build())
                       .collect(Collectors.toList());
    }

    private List<String> findPetInfoForComment(long accountId) {
        return petRepository.findPetsByAccountId(accountId)
                            .stream()
                            .filter(Objects::nonNull)
                            .map(Pet::getBreed)
                            .collect(Collectors.toList());
    }

    private boolean isLeader(Club club, Comment comment) {
        return club.getAccountClubs().stream()
                   .filter(AccountClub::isLeader)
                   .anyMatch(ac -> ac.getAccount().equals(comment.getAccount()));
    }
}
