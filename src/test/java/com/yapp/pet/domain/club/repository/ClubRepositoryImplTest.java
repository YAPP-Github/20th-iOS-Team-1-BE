package com.yapp.pet.domain.club.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleBreed;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.entity.QClub;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.club.model.SearchingClubDto;
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

import static com.yapp.pet.domain.account.entity.QAccount.account;
import static com.yapp.pet.domain.accountclub.entity.QAccountClub.accountClub;
import static com.yapp.pet.domain.club.entity.QClub.club;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
class ClubRepositoryImplTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    @Autowired
    ClubRepository clubRepository;

    @Test
    @DisplayName("검색어를 통해 club을 검색할 때, club의 title에 검색어가 포함된 club이 조회된다")
    void searchClubByWord() throws Exception {

        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubNameContains("산책"))
                                                    .fetch();

        for (SearchingClubDto searchingClubDto : result) {
            assertThat(searchingClubDto.getTitle()).contains("산책");
        }
    }

    @Test
    @DisplayName("산책 카테고리를 검색하면, 산책 카테고리를 가진 club이 조회된다")
    void searchClubByCategory() throws Exception {

        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubCategoryEq(Category.WALK))
                                                    .fetch();

        assertThat(result).extracting("category")
                .containsExactly(Category.WALK);
    }



    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 ALL인 경우, club의 견주 성별 조건 상관없이 club이 조회되야한다")
    void eligibleSexIsAll() throws Exception {
        //given
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubEligibleSexEq(EligibleSex.ALL))
                                                    .fetch();

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
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubEligibleSexEq(man))
                                                    .fetch();

        for (SearchingClubDto searchingClubDto : result) {
            System.out.println("searchingClubDto = " + searchingClubDto.getEligibleSex());
        }

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
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubEligibleSexEq(userFilter))
                                                    .fetch();

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
        SearchingClubDto result = queryFactory.select(
                                                      Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                              accountClub.club.accountClubs.size()))
                                              .from(accountClub)
                                              .join(accountClub.club, club)
                                              .join(accountClub.account, account)
                                              .where(isLeader(accountClub.leader))
                                              .where(clubParticipateRange(limitMin, limitMax))
                                              .fetchFirst();

        assertThat(result.getParticipants()).isBetween(0, 3);
    }

    @Test
    @DisplayName("유저가 고른 견종이 웰시코기이고, club의 견종 조건이 말티즈, 리트리버일 경우 club이 조회되지 않는다")
    void clubEligiblePetTypeNotContainsUserPetTypeFilter() throws Exception {
        //given
        EligibleBreed userFilter = EligibleBreed.WELSH_CORGI;

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubPetTypeExist(userFilter))
                                                    .fetch();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유저가 고른 견종이 말티즈이고, club의 견종 조건이 말티즈, 리트리버일 경우 club이 조회된다")
    void clubEligiblePetTypeContainsUserPetTypeFilter() throws Exception {
        //given
        EligibleBreed userFilter = EligibleBreed.RETRIEVER;

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, QClub.club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubPetTypeExist(userFilter))
                                                    .fetch();

        //then
        assertThat(result).flatExtracting(SearchingClubDto::getEligibleBreeds)
                          .contains(EligibleBreed.MALTESE);
    }

    @Test
    @DisplayName("유저가 고른 견종 크기가 소형견이고, club의 견종 조건이 대형견, 중형견일 경우 club이 조회되지 않는다")
    void clubEligiblePetSizeTypeNotContainsUserPetTypeFilter() throws Exception {
        //given
        PetSizeType userFilter = PetSizeType.SMALL;

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubPetSizeTypeExist(userFilter))
                                                    .fetch();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유저가 고른 견종 크기가 대형견이고, club의 견종 조건이 대형견, 중형견일 경우 club이 조회된다")
    void clubEligiblePetSizeTypeContainsUserPetTypeFilter() throws Exception {
        //given
        PetSizeType userFilter = PetSizeType.LARGE;

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubPetSizeTypeExist(userFilter))
                                                    .fetch();

        //then
        assertThat(result).flatExtracting(SearchingClubDto::getEligiblePetSizeTypes)
                          .contains(PetSizeType.LARGE);
    }

    @Test
    @DisplayName("모임 제목에 산책 포함, 카테고리는 산책, 견종은 상관 없고, 크기는 중형견, 성별은 남녀 상관없고, 인원수는 3명 이하인 모임을 조회할경우 해당 조건을 만족하는 모임이 조회된다")
    void allFilter() throws Exception {
        //given
        String userTitle = "산책";
        Category userCategory = Category.WALK;
        EligibleBreed userBreed = EligibleBreed.ALL;
        PetSizeType userPetSizeType = PetSizeType.MEDIUM;
        EligibleSex userSex = EligibleSex.ALL;
        int limitMin = 0, limitMax = 3;

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                           Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                   accountClub.club.accountClubs.size()))
                                                   .from(accountClub)
                                                   .join(accountClub.club, club)
                                                   .join(accountClub.account, account)
                                                   .where(isLeader(accountClub.leader))
                                                   .where(clubCategoryEq(userCategory))
                                                   .where(clubNameContains(userTitle))
                                                   .where(clubPetTypeExist(userBreed))
                                                   .where(clubPetSizeTypeExist(userPetSizeType))
                                                   .where(clubEligibleSexEq(userSex))
                                                   .where(clubParticipateRange(limitMin, limitMax))
                                                   .fetch();

        System.out.println("result.size() = " + result.size());

        //then
        assertAll(
                () -> assertThat(result).flatExtracting(SearchingClubDto::getEligiblePetSizeTypes)
                                        .contains(userPetSizeType),
                () -> assertThat(result).extracting("category")
                                        .containsExactly(userCategory),
                () -> assertThat(result.get(0).getParticipants()).isBetween(0, 3)
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

    private BooleanExpression clubPetTypeExist(EligibleBreed eligibleBreed) {
        if (eligibleBreed == EligibleBreed.ALL) {
            return null;
        }

        return eligibleBreed == null ? null : club.eligibleBreeds.any().eq(eligibleBreed);
    }
}

