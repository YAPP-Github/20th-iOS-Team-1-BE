package com.yapp.pet.domain.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.club.service.ClubParticipationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql({"/concurrencyTestData.sql"})
@Sql(value = {"/sql/clean-up.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Tag("integrationTest")
@ActiveProfiles("test")
@DisplayName("ClubParticipationConcurrency Integration Test")
public class ClubParticipationConcurrencyTest {

    @Autowired
    ClubParticipationService clubService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AccountClubRepository accountClubRepository;

    Account account;
    final Long CLUB_ID = 1L;

    @BeforeEach
    void init(){
        account = accountRepository.findById(2L).get();
    }

    /*
    * 원래 서로 다른 Account가 모임에 참여해야 하지만, 앱에서는 모임 참여 후 같은 모임에 다시 참여할 수 있는 방법이 없어서
    * 같은 Account가 같은 모임에 참여하지 못하도록 하는 별도의 검증로직이 없습니다. (나중에는 추가 해야겠지만요)
    * 따라서 테스트에서는 하나의 Account를 이용하고, 각 쓰레드가 서로 다른 Account 역할을 수행합니다.
    * */
    @Test
    @DisplayName("인원 제한이 2명인 모임에 1명이 이미 참여중(방장)이고, 남은 1자리에 10명이 동시에 참여하는 상황")
    void participateClubTest() throws InterruptedException {

        //given
        final int PARTICIPATION_PEOPLE = 10;
        final int CLUB_MAXIMUM_PEOPLE = 2;
        CountDownLatch countDownLatch = new CountDownLatch(PARTICIPATION_PEOPLE);

        List<ParticipateWorkerWithPessimisticLock> workers = Stream
                .generate(() -> new ParticipateWorkerWithPessimisticLock(account, countDownLatch))
                .limit(PARTICIPATION_PEOPLE)
                .collect(Collectors.toList());

        //when
        workers.forEach(worker -> new Thread(worker).start());
        countDownLatch.await();

        //then
        Club findClub = clubRepository.findById(CLUB_ID).get();
        long participationAccountCount = findClub.getParticipants();

        assertThat(participationAccountCount).isEqualTo(CLUB_MAXIMUM_PEOPLE);
    }

    private class ParticipateWorkerWithPessimisticLock implements Runnable {
        private Account account;
        private CountDownLatch countDownLatch;

        public ParticipateWorkerWithPessimisticLock(Account account, CountDownLatch countDownLatch) {
            this.account = account;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            clubService.participateClubWithPessimisticLock(CLUB_ID, account);
            countDownLatch.countDown();
        }
    }

    private class ParticipateWorkerWithDistributedLock implements Runnable {
        private Account account;
        private CountDownLatch countDownLatch;

        public ParticipateWorkerWithDistributedLock(Account account, CountDownLatch countDownLatch) {
            this.account = account;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            clubService.participateClubWithDistributedLock(CLUB_ID, account);
            countDownLatch.countDown();
        }
    }

}
