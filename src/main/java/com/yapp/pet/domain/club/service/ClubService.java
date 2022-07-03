package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubRepository;
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
import java.util.List;

import static com.yapp.pet.web.club.model.ClubParticipateRejectReason.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final AccountClubRepository accountClubRepository;
    private final ClubMapper clubMapper;
    private final PetRepository petRepository;
    private final CommentRepository commentRepository;

    public void leaveClub(Long clubId, Account loginAccount) {

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        AccountClub accountClub = findClub.getAccountClubs().stream()
                .filter(ac -> ac.getAccount().equals(loginAccount))
                .findFirst()
                .orElseThrow(NotParticipatingClubException::new);

        accountClubRepository.delete(accountClub);
    }

    public long create(Account account, ClubCreateRequest clubCreateRequest) {

        if (petRepository.findPetsByAccountId(account.getId()).size() <= 0) {
            throw new NotHaveAnyPetException();
        }

        Club club = clubMapper.toEntity(clubCreateRequest);
        clubRepository.save(club);

        AccountClub accountClub = AccountClub.of(account, club);
        accountClub.addClub(club);
        accountClubRepository.save(accountClub);

        return club.getId();
    }

    public void deleteClub(Long clubId, Account loginAccount) {

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

    private boolean isLeader(Club findClub, Account loginAccount) {
        return findClub.getAccountClubs().stream()
                .filter(AccountClub::isLeader)
                .anyMatch(ac -> ac.getAccount().equals(loginAccount));
    }

    public ClubParticipateResponse isEligibleClub(Long clubId, Account loginAccount) {
        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);
        List<Pet> findPets = petRepository.findPetsByAccountId(loginAccount.getId());

        if (hasNotPet(findPets)) {
            return ClubParticipateResponse.of(false, HAS_NOT_PET);
        }

        if (!isEligibleSex(loginAccount, findClub)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_SEX);
        }

        if (!isEligiblePetSizeType(findClub, findPets)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_PET_SIZE_TYPE);
        }

        if (!isEligibleBreeds(findClub, findPets)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_BREEDS);
        }

        return ClubParticipateResponse.of(true, null);
    }

    private boolean hasNotPet(List<Pet> findPets) {
        return findPets == null || findPets.isEmpty();
    }

    private boolean isEligibleSex(Account loginAccount, Club club) {
        AccountSex accountSex = loginAccount.getSex();
        EligibleSex eligibleSex = club.getEligibleSex();

        if (eligibleSex == EligibleSex.ALL) {
            return true;
        }

        if (eligibleSex == EligibleSex.MAN && accountSex == AccountSex.MAN) {
            return true;
        }

        if (eligibleSex == EligibleSex.WOMAN && accountSex == AccountSex.WOMAN) {
            return true;
        }

        return false;
    }

    private boolean isEligiblePetSizeType(Club club, List<Pet> findPets) {
        return findPets.stream()
                .anyMatch(pet -> club.getEligiblePetSizeTypes().contains(pet.getSizeType()));
    }

    private boolean isEligibleBreeds(Club club, List<Pet> findPets) {
        return findPets.stream()
                .anyMatch(pet -> club.getEligibleBreeds().contains(pet.getBreed()));
    }

    public void participateClub(Long clubId, Account loginAccount){
        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        AccountClub accountClub = AccountClub.of(loginAccount, findClub);
        accountClub.addClub(findClub);

        accountClubRepository.save(accountClub);
    }
}
