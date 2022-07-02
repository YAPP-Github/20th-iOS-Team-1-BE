package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.global.exception.club.NotParticipatingClubException;
import com.yapp.pet.global.exception.common.ExceptionStatus;
import com.yapp.pet.global.exception.jwt.NotFoundTokenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
public class ClubServiceTest {

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountClubRepository accountClubRepository;

    @Autowired
    ClubService clubService;

    Account accountWithTokenAndImage;
    Account accountWithTokenWithoutImage;
    Account accountWithoutToken;

    @BeforeEach
    void init(){
        accountWithTokenAndImage = accountRepository.findById(1L).get();
        accountWithTokenWithoutImage = accountRepository.findById(4L).get();

        accountWithoutToken = Account.builder()
                .age(10)
                .sex(AccountSex.MAN)
                .nickname("test")
                .build();
    }

    @Test
    @DisplayName("모임에 참여하지 않은 사용자는 모임을 나갈 수 없다.")
    void canNotLeaveClub(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(3L).get();

        //when & then
        assertThatExceptionOfType(NotParticipatingClubException.class)
                .isThrownBy(() -> clubService.leaveClub(clubId, loginAccount))
                .withMessageMatching(ExceptionStatus.NOT_PARTICIPATING_CLUB_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("모임에 참여중인 사용자는 모임을 나갈 수 있다.")
    void leaveClub(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(2L).get();

        //when
        clubService.leaveClub(clubId, loginAccount);

        //then
        assertThat(accountClubRepository.findById(2L)).isEmpty();
    }

}
