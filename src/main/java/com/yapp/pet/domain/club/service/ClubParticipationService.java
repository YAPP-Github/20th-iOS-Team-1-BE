package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.web.club.model.ClubParticipateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.yapp.pet.global.TogaetherConstants.*;

/*
* 여러 Lock을 사용해보며 학습하기 위해서 별도의 클래스로 분리했습니다.
* */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClubParticipationService {

    private final ClubRepository clubRepository;
    private final AccountClubRepository accountClubRepository;
    private final PetRepository petRepository;

    private final ClubValidator validator;

    private final RedissonClient redissonClient;

    private boolean isFullClub(Club club) {
        return club.getParticipants() == club.getMaximumPeople();
    }

    private ClubParticipateResponse participateClub(Account loginAccount, Club findClub) {

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

    public ClubParticipateResponse participateClubWithoutLock(Long clubId, Account loginAccount) {
        Club findClub = clubRepository.findClubDetailById(clubId).orElseThrow(EntityNotFoundException::new);

        return participateClub(loginAccount, findClub);
    }

    public ClubParticipateResponse participateClubWithPessimisticLock(Long clubId, Account loginAccount) {
        Club findClub = clubRepository.findClubDetailByIdWithLock(clubId).orElseThrow(EntityNotFoundException::new);

        return participateClub(loginAccount, findClub);
    }
}
