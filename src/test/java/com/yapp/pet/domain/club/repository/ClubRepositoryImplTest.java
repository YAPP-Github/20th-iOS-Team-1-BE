package com.yapp.pet.domain.club.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.entity.Address;
import com.yapp.pet.domain.accountclub.entity.AccountClub;
import com.yapp.pet.domain.club.entity.*;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.global.util.DistanceUtil;
import com.yapp.pet.web.club.model.SearchingClubDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yapp.pet.domain.account.entity.QAccount.*;
import static com.yapp.pet.domain.accountclub.entity.QAccountClub.*;
import static com.yapp.pet.domain.club.entity.QClub.club;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Disabled
class ClubRepositoryImplTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    private Account myAccount;
    private Club myClub;
    private AccountClub myAccountClub;

    @Autowired
    ClubRepository clubRepository;

    @BeforeEach
    void init() {
        queryFactory = new JPAQueryFactory(em);
        myAccount = Account.builder()
                           .nickname("member")
                           .age(10)
                           .sex(AccountSex.MAN)
                           .address(new Address("gi", "go"))
                           .build();

        Set<PetSizeType> eligiblePetSizeTypes = Set.of(PetSizeType.LARGE, PetSizeType.MEDIUM);
        Set<EligibleBreed> eligibleBreeds = Set.of(EligibleBreed.MALTESE, EligibleBreed.RETRIEVER);

        em.persist(myAccount);

        for (int i = 0; i < 100; i++) {
            myClub = Club.builder()
                         .title("큐큐랑 산책할사람")
                         .description("설명")
                         .category(Category.WALK)
                         .startDate(ZonedDateTime.now())
                         .endDate(ZonedDateTime.now())
                         .maximumPeople(10 + i)
                         .meetingPlace("서울시")
                         .eligibleSex(EligibleSex.MAN)
                         .eligiblePetSizeTypes(eligiblePetSizeTypes)
                         .eligibleBreeds(eligibleBreeds)
                         .latitude(35.179382 + i * 10)
                         .longitude(126.912465 + i * 10)
                         .build();

            em.persist(myClub);

            myAccountClub = AccountClub.of(myAccount, myClub);

            em.persist(myAccountClub);

            myAccountClub.addClub(myClub);
        }
        em.flush();
        em.clear();
    }

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

        assertThat(result.size()).isEqualTo(10);

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

        assertThat(result.size()).isEqualTo(10);

        assertThat(result).extracting("category")
                          .contains(Category.WALK);
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
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 MAN인 경우, club의 견주 성별 조건이 MAN인 club만 조회되어야한다")
    void userEligibleSexIsMan() throws Exception {
        //given
        EligibleSex userFilter = EligibleSex.MAN;

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
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("유저가 선택한 견주 성별 필터가 WOMAN인 경우, club의 견주 성별 조건이 WOMAN인 club만 조회되어야한다")
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
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("club의 견주 성별 조건이 ALL인 경우, 견주 성별 조건이 ALL인 club이 조회되야한다")
    void clubEligibleSexIsAll() throws Exception {
        //given
        myClub = Club.builder()
                     .title("큐큐랑 산책할사람")
                     .description("설명")
                     .category(Category.WALK)
                     .startDate(ZonedDateTime.now())
                     .endDate(ZonedDateTime.now())
                     .maximumPeople(10)
                     .meetingPlace("서울시")
                     .eligibleSex(EligibleSex.ALL)
                     .latitude(35.179382 + 10)
                     .longitude(126.912465 + 10)
                     .build();

        em.persist(myClub);

        myAccountClub = AccountClub.of(myAccount, myClub);

        em.persist(myAccountClub);

        myAccountClub.addClub(myClub);

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
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저가 고른 인원 필터링 중 3명 이하를 고를 경우에는 club의 인원이 0~3명인 club이 조회되어야한다")
    void clubParticipateLimitRange() throws Exception {
        //given
        int limitMax = 3, limitMin = 0;

        Account account1 = Account.builder()
                                  .nickname("member1")
                                  .age(10)
                                  .sex(AccountSex.MAN)
                                  .address(new Address("gi", "go"))
                                  .build();

        Account account2 = Account.builder()
                                  .nickname("member2")
                                  .age(10)
                                  .sex(AccountSex.MAN)
                                  .address(new Address("gi", "go"))
                                  .build();

        em.persist(account1);
        em.persist(account2);

        AccountClub accountClub1 = AccountClub.of(account1, myClub);
        em.persist(accountClub1);
        accountClub1.addClub(myClub);

        AccountClub accountClub2 = AccountClub.of(account2, myClub);
        em.persist(accountClub2);
        accountClub2.addClub(myClub);

        //when
        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubParticipateRange(limitMin, limitMax))
                                                    .fetch();

        //then
        assertThat(result.size()).isEqualTo(100);
        assertThat(clubRepository.getById(myClub.getId()).getAccountClubs().size()).isEqualTo(3);
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
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("유저가 고른 견종이 말티즈이고, club의 견종 조건이 말티즈, 리트리버일 경우 club이 조회된다")
    void clubEligiblePetTypeContainsUserPetTypeFilter() throws Exception {
        //given
        EligibleBreed userFilter = EligibleBreed.MALTESE;

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
        assertThat(result.size()).isEqualTo(100);
    }

    private BooleanExpression clubPetTypeExist(EligibleBreed eligibleBreed) {
        if (eligibleBreed == EligibleBreed.ALL) {
            return null;
        }

        return eligibleBreed == null ? null : club.eligibleBreeds.any().eq(eligibleBreed);
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
        assertThat(result.size()).isEqualTo(0);
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
        assertThat(result.size()).isEqualTo(100);
    }

    private BooleanExpression clubPetSizeTypeExist(PetSizeType petSizeType) {
        if (petSizeType == PetSizeType.ALL) {
            return null;
        }

        return petSizeType == null ? null : club.eligiblePetSizeTypes.any().eq(petSizeType);
    }

    @Test
    @DisplayName("모임 제목에 산책, 카테고리는 산책, 견종은 말티즈만, 크기는 중형견, 성별은 남녀 상관없고, 인원수는 3명 이하인 모임을 조회할경우 해당 조건을 만족하는 모임이 조회된다")
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

        //then
        assertThat(result.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("모든 조건을 만족하는 club은 유저로부터 가까운 거리에 있는 10개의 club이 조회된다")
    void allFilterAndDistanceAndPaging() throws Exception {
        //given
        String userTitle = "산책";
        Category userCategory = Category.WALK;
        EligibleBreed userBreed = EligibleBreed.ALL;
        PetSizeType userPetSizeType = PetSizeType.MEDIUM;
        EligibleSex userSex = EligibleSex.ALL;
        int limitMin = 0, limitMax = 3;
        int page = 0;
        int pageSize = 10;
        Double userStartLatitude = 23D;
        Double userStartLongitude = 24D;

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
                                                    .offset(page)
                                                    .limit(pageSize)
                                                    .fetch()
                                                    .stream()
                                                    .sorted(
                                                            (club1, club2) -> {
                                                                return DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                           userStartLatitude, club1.getLatitude(),
                                                                                           userStartLongitude, club1.getLongitude())
                                                                                   .compareTo(
                                                                                           DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                                   userStartLatitude,
                                                                                                   club2.getLatitude(),
                                                                                                   userStartLongitude,
                                                                                                   club2.getLongitude()));
                                                            }
                                                    )
                                                    .collect(Collectors.toList());

        //then
        assertThat(result.size()).isEqualTo(10);
    }
}

