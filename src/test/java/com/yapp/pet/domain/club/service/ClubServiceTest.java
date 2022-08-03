package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.comment.CommentRepository;
import com.yapp.pet.global.exception.club.NotLeaderException;
import com.yapp.pet.global.exception.club.NotParticipatingClubException;
import com.yapp.pet.global.exception.common.ExceptionStatus;
import com.yapp.pet.web.club.model.ClubParticipateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.yapp.pet.web.club.model.ClubParticipateRejectReason.*;
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
    CommentRepository commentRepository;

    @Autowired
    ClubService clubService;

    @Autowired
    ClubParticipationService clubParticipationService;

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

    @Test
    @DisplayName("모임의 방장은 모임을 삭제할 수 있다. 댓글도 함께 삭제된다.")
    void deleteClub(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(1L).get();
        Club findClub = clubRepository.findById(clubId).get();

        //when
        clubService.deleteClub(clubId, loginAccount);

        //then
        assertThat(clubRepository.findById(clubId)).isEmpty();

        assertThat(accountClubRepository.findAllById(
                    findClub.getAccountClubs().stream()
                            .map(AccountClub::getId)
                            .collect(Collectors.toList())
        )).isEmpty();

        assertThat(commentRepository.findCommentByClubId(clubId)).isEmpty();
    }

    @Test
    @DisplayName("모임의 방장이 아니면 모임을 삭제할 수 없다.")
    void canNotDeleteClub(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(2L).get();

        //when & then
        assertThatExceptionOfType(NotLeaderException.class)
                .isThrownBy(() -> clubService.deleteClub(clubId, loginAccount))
                .withMessageMatching(ExceptionStatus.NOT_LEADER_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("모임 참여 실패 - 반려견 미등록시 모임에 참여할 수 없다.")
    void notParticipateClubByNotPet(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(3L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isFalse();
        assertThat(response.getRejectReason()).isEqualTo(HAS_NOT_PET);
    }

    @Test
    @DisplayName("모임 참여 실패 - 모임에 설정된 성별과 다르면 참여할 수 없다.")
    void notParticipateClubByNotEligibleSex(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(2L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isFalse();
        assertThat(response.getRejectReason()).isEqualTo(NOT_ELIGIBLE_SEX);
    }

    @Test
    @DisplayName("모임 참여 실패 - 모임에 설정된 반려견 크기를 만족하는 반려견이 한마리도 없을 시 참여할 수 없다.")
    void notParticipateClubByNotEligiblePetSizeType(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(5L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isFalse();
        assertThat(response.getRejectReason()).isEqualTo(NOT_ELIGIBLE_PET_SIZE_TYPE);
    }

    @Test
    @DisplayName("모임 참여 실패 - 모임에 설정된 견종을 만족하는 반려견이 한마리도 없을 시 참여할 수 없다.")
    void notParticipateClubByNotEligibleBreeds() {
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(4L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isFalse();
        assertThat(response.getRejectReason()).isEqualTo(NOT_ELIGIBLE_BREEDS);
    }

    @Test
    @DisplayName("모임 참여 실패 - 인원이 다 찬 모임은 참여할 수 없다.")
    void notParticipateClubByFull() {
        //given
        Long clubId = 8L;
        Account loginAccount = accountRepository.findById(4L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isFalse();
        assertThat(response.getRejectReason()).isEqualTo(FULL);
    }

    @Test
    @DisplayName("모임 참여 성공 - 모든 조건을 만족하고 모임에 참여할 수 있다.")
    void participateClub(){
        //given
        Long clubId = 1L;
        Account loginAccount = accountRepository.findById(6L).get();

        //when
        ClubParticipateResponse response = clubParticipationService.participateClubWithPessimisticLock(clubId, loginAccount);

        //then
        assertThat(response.isEligible()).isTrue();
        assertThat(response.getRejectReason()).isNull();
    }

}
