package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.global.exception.club.NotLeaderException;
import com.yapp.pet.global.exception.club.NotParticipatingClubException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final AccountClubRepository accountClubRepository;
    private final CommentRepository commentRepository;

    public void leaveClub(Long clubId, Account loginAccount){

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        AccountClub accountClub = findClub.getAccountClubs().stream()
                .filter(ac -> ac.getAccount().equals(loginAccount))
                .findFirst()
                .orElseThrow(NotParticipatingClubException::new);

        accountClubRepository.delete(accountClub);
    }

    public void deleteClub(Long clubId, Account loginAccount){

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        if (!isLeader(findClub, loginAccount)) {
            throw new NotLeaderException();
        }

        commentRepository.deleteAllInBatch(
                commentRepository.findCommentByClubId(findClub.getId())
        );
        accountClubRepository.deleteAllInBatch(findClub.getAccountClubs());
        clubRepository.delete(findClub);
    }

    private boolean isLeader(Club findClub, Account loginAccount){
        return findClub.getAccountClubs().stream()
                .filter(AccountClub::isLeader)
                .anyMatch(ac -> ac.getAccount().equals(loginAccount));
    }

}
