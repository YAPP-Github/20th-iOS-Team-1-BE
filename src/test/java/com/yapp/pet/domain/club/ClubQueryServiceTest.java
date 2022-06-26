package com.yapp.pet.domain.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.club.model.ClubFindResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.yapp.pet.domain.club.repository.ClubFindCondition.*;
import static com.yapp.pet.web.club.model.ClubFindResponse.*;
import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
class ClubQueryServiceTest {

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClubQueryService clubQueryService;

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
    @DisplayName("검색 타입이 카테고리일 경우 카테고리가 같은 모임 중에 사용자로부터 가까운 모임 순서대로 조회한다")
    void searchingClubByCategory() throws Exception {
        //given
        SearchingRequest request = new SearchingRequest();

        request.setCategory(Category.WALK);
        request.setEligibleBreed("상관없음");
        request.setPetSizeType(PetSizeType.MEDIUM);
        request.setEligibleSex(EligibleSex.ALL);
        request.setParticipateMax(3);
        request.setParticipateMin(0);
        request.setStartLatitude(37.504757);
        request.setStartLongitude(126.980149);
        String searchingType = "word";

        //when
        List<SearchingResponse> result = clubQueryService.searchingClub(request, "category");

        //then
        assertThat(result).extracting("category")
                          .contains(Category.WALK);

        assertThat(result).extracting("distance")
                          .isSorted();
    }

    @Test
    @DisplayName("검색 타입이 검색어일 경우 모임 이름 중 검색어가 포함된 모임 중에 사용자로부터 가까운 모임 순서대로 조회한다")
    void searchingClubByWord() throws Exception {
        //given
        SearchingRequest request = new SearchingRequest();

        request.setSearchingWord("산책");
        request.setEligibleBreed("상관없음");
        request.setPetSizeType(PetSizeType.MEDIUM);
        request.setEligibleSex(EligibleSex.ALL);
        request.setParticipateMax(3);
        request.setParticipateMin(0);
        request.setStartLatitude(37.504757);
        request.setStartLongitude(126.980149);
        String searchingType = "category";

        //when
        List<SearchingResponse> result = clubQueryService.searchingClub(request, "word");

        //then
        assertThat(result.get(0).getTitle()).contains("산책");

        assertThat(result).extracting("distance")
                          .isSorted();
    }

    @Test
    @DisplayName("모임 종료 시간이 지난 모임들을 조회할 수 있다")
    void convertStatusAvailableToEnd() throws Exception {
        //when
        List<Club> savedClubs = clubQueryService.exceedTimeClub();

        //then
        assertThat(savedClubs.get(0).getEndDate()).isBefore(ZonedDateTime.now());
    }

    @Test
    @DisplayName("좌상단, 우하단 위도 경도가 주어질 시 그 범위 내에 있는 모임들이 조회된다")
    void searchingClubWithinRange() throws Exception {
        //given
        Double upperLeftLatitude = 37.528176;
        Double upperLeftLongitude = 126.953678;
        Double bottomRightLatitude = 37.503199;
        Double bottomRightLongitude = 126.991272;

        SearchingWithinRangeClubRequest request = new SearchingWithinRangeClubRequest(
                upperLeftLatitude, upperLeftLongitude, bottomRightLatitude, bottomRightLongitude
        );

        //when
        List<SearchingWithinRangeClubResponse> result = clubQueryService.searchingRangeClub(request);

        SearchingWithinRangeClubResponse firstResult = result.get(0);

        //then
        assertAll(
                () -> assertTrue(firstResult.getClubLatitude() >= bottomRightLatitude &&
                                         firstResult.getClubLatitude() <= upperLeftLatitude),
                () -> assertTrue(firstResult.getClubLongitude() >= upperLeftLongitude &&
                                         firstResult.getClubLongitude() <= bottomRightLongitude)
        );
    }

    @Test
    @DisplayName("챰여중인 모임목록을 처음 조회한다.")
    void findClubsByParticipatingAtFirst() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 종료되지않고 참여중인 club은 1,4,5
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(null, I_AM_PARTICIPATING, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();
        List<ClubInfo> content = clubInfos.getContent();
        ClubInfo clubInfo1 = content.get(0);
        ClubInfo clubInfo2 = content.get(1);

        //then
        assertThat(response.getHasNotClub()).isFalse();
        assertThat(clubInfos.getTotalElements()).isEqualTo(2);

        // curosrId가 null이므로 endDate 기준 오름차순 조회
        assertThat(clubInfo1.getClubId()).isEqualTo(1);
        assertThat(clubInfo2.getClubId()).isEqualTo(4);

        assertThat(clubInfo1.getTitle()).isEqualTo("쿄쿄량 산책할사람");
        assertThat(clubInfo1.getMaximumPeople()).isEqualTo(2);
        assertThat(clubInfo1.getParticipants()).isEqualTo(2);
        assertThat(clubInfo1.getEligiblePetSizeTypes().size()).isEqualTo(2); // LARGE, MEDIUM
        assertThat(clubInfo1.getEligibleBreeds().size()).isEqualTo(2); // 말티즈, 리트리버
        assertThat(clubInfo1.getEndDate().getYear()).isEqualTo(2021);
        assertThat(clubInfo1.getEndDate().getMonthValue()).isEqualTo(1);
        assertThat(clubInfo1.getEndDate().getDayOfMonth()).isEqualTo(2);

        assertThat(clubInfo2.getTitle()).isEqualTo("모임ㅁㅁㅁ");
        assertThat(clubInfo2.getMaximumPeople()).isEqualTo(3);
        assertThat(clubInfo2.getParticipants()).isEqualTo(1);
        assertThat(clubInfo2.getEligiblePetSizeTypes().size()).isEqualTo(1); // SMALL
        assertThat(clubInfo2.getEligibleBreeds().size()).isEqualTo(0);
        assertThat(clubInfo2.getEndDate().getYear()).isEqualTo(2022);
        assertThat(clubInfo2.getEndDate().getMonthValue()).isEqualTo(7);
        assertThat(clubInfo2.getEndDate().getDayOfMonth()).isEqualTo(1);
    }

    @Test
    @DisplayName("챰여중인 모임목록에서 아래로 스크롤을 한번 내려서 다음 목록을 조회한다.")
    void findClubsByParticipatingAtNext() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 종료되지않고 참여중인 club은 1,4,5
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(4L, I_AM_PARTICIPATING, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();
        List<ClubInfo> content = clubInfos.getContent();
        ClubInfo clubInfo = content.get(0);

        //then
        assertThat(response.getHasNotClub()).isFalse();
        assertThat(clubInfos.getTotalElements()).isEqualTo(1);
        assertThat(clubInfo.getClubId()).isEqualTo(5); // cursorId가 4이므로 5부터 조회
    }

    @Test
    @DisplayName("챰여중인 모임목록에서 마지막 페이지에서 스크롤을 아래로 내린다.")
    void findClubsByParticipatingAtLast() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 종료되지않고 참여중인 club은 1,4,5
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(5L, I_AM_PARTICIPATING, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();

        //then
        assertThat(response.getHasNotClub()).isTrue();
        assertThat(clubInfos.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("내가 만든 모임목록을 처음 조회한다.")
    void findClubsByIAmLeaderAtFirst() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 만든 클럽 중 종료되지 않은 클럽은 1,4,5
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(null, I_AM_LEADER, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();
        List<ClubInfo> content = clubInfos.getContent();
        ClubInfo clubInfo1 = content.get(0);
        ClubInfo clubInfo2 = content.get(1);

        //then
        assertThat(response.getHasNotClub()).isFalse();
        assertThat(clubInfos.getTotalElements()).isEqualTo(2);

        // curosrId가 null이므로 endDate 기준 오름차순 조회
        assertThat(clubInfo1.getClubId()).isEqualTo(1);
        assertThat(clubInfo2.getClubId()).isEqualTo(4);
    }

    @Test
    @DisplayName("내가 만든 모임목록에서 아래로 스크롤을 한번 내려서 다음 목록을 조회한다.")
    void findClubsByIAmLeaderAtNext() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 만든 클럽 중 종료되지 않은 클럽은 1,4,5
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(4L, I_AM_LEADER, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();
        List<ClubInfo> content = clubInfos.getContent();
        ClubInfo clubInfo = content.get(0);

        //then
        assertThat(response.getHasNotClub()).isFalse();
        assertThat(clubInfos.getTotalElements()).isEqualTo(1);
        assertThat(clubInfo.getClubId()).isEqualTo(5); // cursorId가 4이므로 5부터 조회
    }


    @Test
    @DisplayName("내가 참여했고, 종료된 모임목록을 처음 조회한다.")
    void findClubsByParticipatedAndExceedAtFirst() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 참여했고, 종료된 클럽은 6,7
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(null, I_AM_PARTICIPATED_AND_EXCEED, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();
        List<ClubInfo> content = clubInfos.getContent();
        ClubInfo clubInfo1 = content.get(0);
        ClubInfo clubInfo2 = content.get(1);

        System.out.println();
        //then
        assertThat(response.getHasNotClub()).isFalse();
        assertThat(clubInfos.getTotalElements()).isEqualTo(2);

        // curosrId가 null이므로 endDate 기준 오름차순 조회
        assertThat(clubInfo1.getClubId()).isEqualTo(6);
        assertThat(clubInfo2.getClubId()).isEqualTo(7);
    }


    @Test
    @DisplayName("내가 참여했고, 종료된 모임목록에서, 마지막 페이지에서 스크롤을 내린다.")
    void findClubsByParticipatedAndExceedAtNext() {
        //given, 실제로는 10개씩 페이징 하지만 테스트에선 2로 테스트
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(ASC, "endDate"));

        //when, 현재 accountWithTokenAndImage가 참여했고, 종료된 클럽은 6,7
        ClubFindResponse response
                = clubQueryService.findClubsByCondition(7L, I_AM_PARTICIPATED_AND_EXCEED, accountWithTokenAndImage, pageRequest);

        Page<ClubInfo> clubInfos = response.getClubInfos();

        //then
        assertThat(response.getHasNotClub()).isTrue();
        assertThat(clubInfos.getTotalElements()).isEqualTo(0);
    }

}