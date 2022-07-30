package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.document.ClubDocument;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.club.repository.elasticsearch.ClubSearchRepository;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.global.exception.club.NotHaveAnyPetException;
import com.yapp.pet.global.exception.club.NotLeaderException;
import com.yapp.pet.global.exception.club.NotParticipatingClubException;
import com.yapp.pet.global.mapper.ClubMapper;
import com.yapp.pet.web.club.model.ClubCreateRequest;
import com.yapp.pet.web.club.model.ClubParticipateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final AccountClubRepository accountClubRepository;
    private final ClubMapper clubMapper;
    private final PetRepository petRepository;
    private final CommentRepository commentRepository;
    private final ClubSearchRepository clubSearchRepository;

    private final ClubValidator validator;

    public Long leaveClub(Long clubId, Account loginAccount) {

        Club findClub = clubRepository.findClubDetailById(clubId).orElseThrow(EntityNotFoundException::new);

        AccountClub accountClub = findClub.getAccountClubs().stream()
                .filter(ac -> ac.getAccount().equals(loginAccount))
                .findFirst()
                .orElseThrow(NotParticipatingClubException::new);

        accountClubRepository.delete(accountClub);

        if (isFullClub(findClub)) {
            findClub.updateStatus(ClubStatus.AVAILABLE);
        }

        findClub.subtractPerson();

        return accountClub.getId();
    }

    private boolean isFullClub(Club club) {
        return club.getParticipants() == club.getMaximumPeople();
    }

    public long createClub(Account account, ClubCreateRequest clubCreateRequest) {

        if (hasNotPet(account)) {
            throw new NotHaveAnyPetException();
        }

        Club club = clubMapper.toEntity(clubCreateRequest);
        clubRepository.save(club);

        AccountClub accountClub = AccountClub.of(account, club);
        club.addAccountClub(accountClub);

        accountClubRepository.save(accountClub);

        return club.getId();
    }

    private boolean hasNotPet(Account account) {
        return petRepository.findPetsByAccountId(account.getId()).size() <= 0;
    }

    public long createClubDocument(long clubId) {
        ClubDocument clubDocument = clubRepository.findById(clubId)
                                                  .map(ClubDocument::of)
                                                  .orElseThrow(EntityNotFoundException::new);

        ClubDocument savedClubDocument = clubSearchRepository.save(clubDocument);

        return savedClubDocument.getId();
    }

    public Long deleteClub(Long clubId, Account loginAccount) {

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        if (!isLeader(findClub, loginAccount)) {
            throw new NotLeaderException();
        }

        commentRepository.deleteAllInBatch(
                commentRepository.findCommentByClubId(findClub.getId())
        );
        accountClubRepository.deleteAllInBatch(findClub.getAccountClubs());
        clubRepository.delete(findClub);

        return findClub.getId();
    }

    public void deleteClub(Long clubId){

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        commentRepository.deleteAllInBatch(
                commentRepository.findCommentByClubId(findClub.getId())
        );
        accountClubRepository.deleteAllInBatch(findClub.getAccountClubs());
        clubRepository.delete(findClub);
    }

    private boolean isLeader(Club findClub, Account loginAccount) {
        return findClub.getAccountClubs().stream()
                .filter(AccountClub::isLeader)
                .anyMatch(ac -> ac.getAccount().equals(loginAccount));
    }

    public ClubParticipateResponse participateClub(Long clubId, Account loginAccount) {
        Club findClub = clubRepository.findClubDetailByIdWithLock(clubId).orElseThrow(EntityNotFoundException::new);
        List<Pet> findPets = petRepository.findPetsByAccountId(loginAccount.getId());

        ClubParticipateResponse response = validator.participationValidate(findClub, findPets, loginAccount);
        response.setClubId(findClub.getId());

        if (!response.isEligible()) {
            return response;
        }

        AccountClub accountClub = AccountClub.of(loginAccount, findClub);
        accountClubRepository.save(accountClub);
        response.setAccountClubId(accountClub.getId());

        findClub.addAccountClub(accountClub);

        if (isFullClub(findClub)) {
            findClub.updateStatus(ClubStatus.PERSONNEL_FULL);
        }

        return response;
    }

    public void updateAccountClubDocument(Long accountClubId) {
        AccountClub accountClub = accountClubRepository.findById(accountClubId)
                                                       .orElseThrow(EntityNotFoundException::new);

        ClubDocument clubDocument = clubSearchRepository.findById(accountClub.getClub().getId())
                                                        .orElseThrow(EntityNotFoundException::new);

        clubDocument.updateAccountClubDocument(accountClub);

        clubSearchRepository.save(clubDocument);
    }
}
