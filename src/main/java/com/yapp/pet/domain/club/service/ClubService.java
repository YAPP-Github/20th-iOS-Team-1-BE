package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.global.exception.club.NotParticipatingClubException;
import com.yapp.pet.global.mapper.ClubMapper;
import com.yapp.pet.web.club.model.ClubCreateRequest;
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
    private final ClubMapper clubMapper;

    public void leaveClub(Long clubId, Account loginAccount){

        Club findClub = clubRepository.findById(clubId).orElseThrow(EntityNotFoundException::new);

        AccountClub accountClub = findClub.getAccountClubs().stream()
                .filter(ac -> ac.getAccount().equals(loginAccount))
                .findFirst()
                .orElseThrow(NotParticipatingClubException::new);

        accountClubRepository.delete(accountClub);
    }

    public long create(Account account, ClubCreateRequest clubCreateRequest) {

        Club club = clubMapper.toEntity(clubCreateRequest);
        clubRepository.save(club);

        AccountClub accountClub = AccountClub.of(account, club);
        accountClub.addClub(club);
        accountClubRepository.save(accountClub);

        return club.getId();
    }
}
