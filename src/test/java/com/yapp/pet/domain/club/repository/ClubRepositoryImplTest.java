package com.yapp.pet.domain.club.repository;

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
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
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
    @DisplayName("검색어를 통해 club을 검색할 때, club의 title에 검색어가 포함된 club 중 사용자와 거리가 가장 가까운 10개의 club을 조회한다")
    void searchClubByWord() throws Exception {

        Double startLatitude = 23D;
        Double endLongitude = 24D;

        long pageStart = 9L;
        long pageSize = 10L;

        List<SearchingClubDto> result = queryFactory.select(Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubNameContains("산책"))
                                                    .offset(pageStart)
                                                    .limit(pageSize)
                                                    .fetch()
                                                    .stream()
                                                    .sorted(
                                                            (o1, o2) -> {
                                                                return DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                           startLatitude, o1.getLatitude(),
                                                                                           endLongitude, o1.getLongitude())
                                                                                   .compareTo(
                                                                                           DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                                   startLatitude,
                                                                                                   o2.getLatitude(),
                                                                                                   endLongitude,
                                                                                                   o2.getLongitude()));
                                                            }
                                                    )
                                                    .collect(Collectors.toList());

        assertThat(result.size()).isEqualTo(10);

        for (SearchingClubDto searchingClubDto : result) {
            assertThat(searchingClubDto.getTitle()).contains("산책");
        }
    }

    @Test
    @DisplayName("카테고리를 통해 검색하면, 같은 category를 가진 club 중 사용자와 거리가 가장 가까운 10개의 club을 조회된다")
    void searchClubByCategory() throws Exception {
        Double startLatitude = 23D;
        Double endLongitude = 24D;

        long pageStart = 9L;
        long pageSize = 10L;

        List<SearchingClubDto> result = queryFactory.select(
                                                            Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname,
                                                                                    accountClub.club.accountClubs.size()))
                                                    .from(accountClub)
                                                    .join(accountClub.club, club)
                                                    .join(accountClub.account, account)
                                                    .where(isLeader(accountClub.leader))
                                                    .where(clubCategoryEq(Category.WALK))
                                                    .offset(pageStart)
                                                    .limit(pageSize)
                                                    .fetch()
                                                    .stream()
                                                    .sorted(
                                                            (o1, o2) -> {
                                                                return DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                           startLatitude, o1.getLatitude(),
                                                                                           endLongitude, o1.getLongitude())
                                                                                   .compareTo(
                                                                                           DistanceUtil.getDistanceBetweenUserAndClub(
                                                                                                   startLatitude,
                                                                                                   o2.getLatitude(),
                                                                                                   endLongitude,
                                                                                                   o2.getLongitude()));
                                                            }
                                                    )
                                                    .collect(Collectors.toList());

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
}

