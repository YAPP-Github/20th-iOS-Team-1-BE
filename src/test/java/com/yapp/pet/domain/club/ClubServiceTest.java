package com.yapp.pet.domain.club;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
class ClubServiceTest {

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubQueryService clubQueryService;

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
}