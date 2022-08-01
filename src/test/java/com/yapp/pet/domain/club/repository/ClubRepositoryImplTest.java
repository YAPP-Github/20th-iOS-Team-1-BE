package com.yapp.pet.domain.club.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.support.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.domain.accountclub.QAccountClub.accountClub;
import static com.yapp.pet.domain.club.entity.QClub.club;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ClubRepositoryImpl Integration Test")
class ClubRepositoryImplTest extends AbstractIntegrationTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    @Autowired
    ClubRepository clubRepository;

    @Test
    @DisplayName("검색어를 통해 club을 검색할 때, club의 title에 검색어가 포함된 club이 조회된다")
    void searchClubByWord() throws Exception {

        List<Club> result = queryFactory.selectFrom(accountClub)
                                    .join(accountClub.club, club).fetchJoin()
                                    .where(isLeader(accountClub.leader))
                                    .where(clubNameContains("산책"))
                                    .fetch()
                                    .stream()
                                    .map(AccountClub::getClub)
                                    .collect(Collectors.toList());

        for (Club club1 : result) {
            assertThat(club1.getTitle()).contains("산책");
        }
    }

    @Test
    @DisplayName("산책 카테고리를 검색하면, 산책 카테고리를 가진 club이 조회된다")
    void searchClubByCategory() throws Exception {

        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubCategoryEq(Category.WALK))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        assertThat(result).extracting("category")
                .containsExactly(Category.WALK);
    }

    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 ALL인 경우, club의 견주 성별 조건 상관없이 club이 조회되야한다")
    void eligibleSexIsAll() throws Exception {
        //given

        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubEligibleSexEq(EligibleSex.ALL))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        //then
        assertThat(result).extracting("eligibleSex")
                          .contains(EligibleSex.MAN)
                          .contains(EligibleSex.ALL)
                          .contains(EligibleSex.WOMAN);
    }

    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 MAN인 경우, club의 견주 성별 조건이 MAN과 ALL인 club이 조회되어야한다")
    void userEligibleSexIsMan() throws Exception {
        //given
        EligibleSex man = EligibleSex.MAN;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubEligibleSexEq(man))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        //then
        assertThat(result).extracting("eligibleSex")
                          .contains(EligibleSex.MAN)
                          .contains(EligibleSex.ALL);
    }

    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 WOMAN인 경우, club의 견주 성별 조건이 WOMAN, ALL인 club이 조회되어야한다")
    void userEligibleSexIsWoman() throws Exception {
        //given
        EligibleSex userFilter = EligibleSex.WOMAN;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubEligibleSexEq(userFilter))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        //then
        assertThat(result).extracting("eligibleSex")
                          .contains(EligibleSex.WOMAN)
                          .contains(EligibleSex.ALL);
    }

    @Test
    @DisplayName("유저가 고른 인원 필터링 중 3명 이하를 고를 경우에는 club의 인원이 0~3명인 club이 조회되어야한다")
    void clubParticipateLimitRange() throws Exception {
        //given
        int limitMax = 3, limitMin = 0;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubParticipateRange(limitMin, limitMax))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        assertThat(result.get(0).getAccountClubs().size()).isBetween(0, 3);
    }

    @Test
    @DisplayName("유저가 고른 견종이 웰시코기이고, club의 견종 조건이 말티즈, 리트리버일 경우 club이 조회되지 않는다")
    void clubEligiblePetTypeNotContainsUserPetTypeFilter() throws Exception {
        //given
        String userFilter = "웰시코기";

        //when

        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubPetTypeExist(userFilter))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        //then버
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유저가 고른 견종이 말티즈이고, club의 견종 조건이 말티즈, 리트리버일 경우 club이 조회된다")
    void clubEligiblePetTypeContainsUserPetTypeFilter() throws Exception {
        //given
        String userFilter = "말티즈";

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                        .join(accountClub.club, club).fetchJoin()
                                        .where(isLeader(accountClub.leader))
                                        .where(clubPetTypeExist(userFilter))
                                        .fetch()
                                        .stream()
                                        .map(AccountClub::getClub)
                                        .collect(Collectors.toList());

        //then
        assertThat(result).flatExtracting("eligibleBreeds")
                          .contains("말티즈");
    }

    @Test
    @DisplayName("유저가 고른 견종 크기가 소형견이고, club의 견종 조건이 대형견, 중형견일 경우 club이 조회되지 않는다")
    void clubEligiblePetSizeTypeNotContainsUserPetTypeFilter() throws Exception {
        //given
        PetSizeType userFilter = PetSizeType.SMALL;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubPetSizeTypeExist(userFilter))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유저가 고른 견종 크기가 대형견이고, club의 견종 조건이 대형견, 중형견일 경우 club이 조회된다")
    void clubEligiblePetSizeTypeContainsUserPetTypeFilter() throws Exception {
        //given
        PetSizeType userFilter = PetSizeType.LARGE;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                        .join(accountClub.club, club).fetchJoin()
                                        .where(isLeader(accountClub.leader))
                                        .where(clubPetSizeTypeExist(userFilter))
                                        .where(clubPetTypeExist("말티즈"))
                                        .fetch()
                                        .stream()
                                        .map(AccountClub::getClub)
                                        .collect(Collectors.toList());

        //then
        assertThat(result).flatExtracting("eligiblePetSizeTypes")
                          .contains(PetSizeType.LARGE);
    }

    @Test
    @DisplayName("모임 제목에 산책 포함, 카테고리는 산책, 견종은 상관 없고, 크기는 중형견, 성별은 남녀 상관없고, 인원수는 3명 이하인 모임을 조회할경우 해당 조건을 만족하는 모임이 조회된다")
    void allFilter() throws Exception {
        //given
        String userTitle = "산책";
        Category userCategory = Category.WALK;
        String userBreed = "상관없음";
        PetSizeType userPetSizeType = PetSizeType.MEDIUM;
        EligibleSex userSex = EligibleSex.ALL;
        int limitMin = 0, limitMax = 3;

        //when
        List<Club> result = queryFactory.selectFrom(accountClub)
                                         .join(accountClub.club, club).fetchJoin()
                                         .where(isLeader(accountClub.leader))
                                         .where(clubCategoryEq(userCategory))
                                         .where(clubNameContains(userTitle))
                                         .where(clubPetTypeExist(userBreed))
                                         .where(clubPetSizeTypeExist(userPetSizeType))
                                         .where(clubEligibleSexEq(userSex))
                                         .where(clubParticipateRange(limitMin, limitMax))
                                         .fetch()
                                         .stream()
                                         .map(AccountClub::getClub)
                                         .collect(Collectors.toList());

        System.out.println("result.size() = " + result.size());

        //then
        assertAll(
                () -> assertThat(result).flatExtracting("eligiblePetSizeTypes")
                                        .contains(userPetSizeType),
                () -> assertThat(result).extracting("category")
                                        .containsExactly(userCategory),
                () -> assertThat(result.get(0).getAccountClubs().size()).isBetween(0, 3)
        );
    }

    @Test
    @DisplayName("모임 종료 시간이 지났는데 현재 상태가 AVAILABLE한 모임을 조회한다")
    void exceedTimeClub() throws Exception {
        List<Club> result = queryFactory.selectFrom(club)
                                        .where(clubStatusEq(ClubStatus.AVAILABLE).and(
                                                club.endDate.after(
                                                        ZonedDateTime.of(1997, 9, 23, 19, 30, 0, 0,
                                                                         ZoneId.of("Asia/Seoul")))))
                                        .fetch();

        assertThat(result).extracting("status")
                          .contains(ClubStatus.AVAILABLE);
    }

    @Test
    @DisplayName("좌상단, 우하단 위도 경도가 주어질 경우 그 범위 내에 있는 모임들이 모두 조회되어야한다")
    void withinRangeClub() throws Exception {
        //given
        Double upperLeftLatitude = 37.528176;
        Double upperLeftLongitude = 126.953678;
        Double bottomRightLatitude = 37.503199;
        Double bottomRightLongitude = 126.991272;

        //when
        List<SearchingWithinRangeClubResponse> result = queryFactory.select(Projections.constructor(
                                                                            SearchingWithinRangeClubResponse.class,
                                                                            club.id, club.category, club.latitude, club.longitude))
                                                                    .from(club)
                                                                    .where(clubWithinRange(upperLeftLatitude,
                                                                                           upperLeftLongitude,
                                                                                           bottomRightLatitude,
                                                                                           bottomRightLongitude))
                                                                    .fetch();

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
    @DisplayName("좌상단, 우하단 위도 경도가 주어질 경우 그 범위 내에 없는 모임들은 조회되지 않는다")
    void notWithinRangeClub() throws Exception {
        //given
        Double upperLeftLatitude = 37.507409;
        Double upperLeftLongitude = 126.975763;
        Double bottomRightLatitude = 37.500957;
        Double bottomRightLongitude = 126.980657;

        //when
        List<SearchingWithinRangeClubResponse> result = queryFactory.select(
                                                                       Projections.constructor(SearchingWithinRangeClubResponse.class,
                                                                                               club.id, club.category, club.latitude, club.longitude))
                                                               .from(club)
                                                               .where(clubWithinRange(upperLeftLatitude, upperLeftLongitude,
                                                                                      bottomRightLatitude,
                                                                                      bottomRightLongitude))
                                                               .fetch();

        //then
        assertThat(result).isEmpty();
    }

    private BooleanExpression clubWithinRange(Double upperLeftLatitude, Double upperLeftLongitude,
                                              Double bottomRightLatitude, Double bottomRightLongitude) {

        return club.latitude.between(bottomRightLatitude, upperLeftLatitude)
                            .and(club.longitude.between(upperLeftLongitude, bottomRightLongitude));
    }

    private BooleanExpression clubStatusEq(ClubStatus clubStatus) {
        return clubStatus == null ? null : club.status.eq(clubStatus);
    }

    private BooleanExpression isLeader(BooleanPath leader) {
        return leader.isTrue();
    }

    private BooleanExpression clubNameContains(String searchingWord) {
        return StringUtils.hasText(searchingWord) ? club.title.contains(searchingWord) : null;
    }

    private BooleanExpression clubCategoryEq(Category category) {
        return category == null ? null : club.category.eq(category);
    }

    private BooleanExpression clubPetSizeTypeExist(PetSizeType petSizeType) {
        if (petSizeType == PetSizeType.ALL) {
            return null;
        }

        return petSizeType == null ? null : club.eligiblePetSizeTypes.any().eq(petSizeType);
    }

    private Predicate clubEligibleSexEq(EligibleSex eligibleSex) {

        if (eligibleSex == null) {
            return null;
        }

        //유저가 고른 필터가 ALL일 경우에는 어떤 클럽이든 다 가능하다
        if (eligibleSex == EligibleSex.ALL) {
            return null;
        }

        //클럽의 조건 중에 모든 성별이 가능하다고 하면 유저가 고른 필터에 상관없다
        BooleanBuilder clubEligibleSexAll = new BooleanBuilder(club.eligibleSex.eq(EligibleSex.ALL)).and(null);

        //둘 다 아닐 경우에는 필터가 맞아야함
        BooleanBuilder clubEligibleSexEqFilter = new BooleanBuilder(club.eligibleSex.ne(EligibleSex.ALL)).and(club.eligibleSex.eq(eligibleSex));

        return new BooleanBuilder(clubEligibleSexAll).or(clubEligibleSexEqFilter);
    }

    private BooleanExpression clubParticipateRange(Integer min, Integer max) {
        if(min == null || max == null) {
            return null;
        }

        return club.accountClubs.size().between(min, max);
    }

    private BooleanExpression clubPetTypeExist(String eligibleBreed) {
        if (eligibleBreed.equals("상관없음")) {
            return null;
        }

        return eligibleBreed == null ? null : club.eligibleBreeds.any().eq(eligibleBreed);
    }
}

