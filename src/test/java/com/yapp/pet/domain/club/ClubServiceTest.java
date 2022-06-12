package com.yapp.pet.domain.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.entity.Address;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleBreed;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.club.model.SearchingClubDto;
import com.yapp.pet.web.club.model.SearchingWithinRangeClubDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
class ClubServiceTest {

    @Autowired
    EntityManager em;

    private Account myAccount;
    private Club myClub;
    private AccountClub myAccountClub;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubService clubService;

    @BeforeEach
    void init() {
        myAccount = Account.builder()
                           .nickname("member1")
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
                         .endDate(ZonedDateTime.of(2021, 5, 21, 19, 30, 0, 0,
                                                   ZoneId.of("Asia/Seoul")))
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
    @DisplayName("검색 타입이 카테고리일 경우 카테고리가 같은 모임 중에 사용자로부터 가까운 모임 10개를 조회한다")
    void searchingClubByCategory() throws Exception {
        //given
        SearchingClubDto.SearchingRequest request = new SearchingClubDto.SearchingRequest();

        request.setCategory(Category.WALK);
        request.setStartLatitude(23D);
        request.setStartLongitude(24D);

        //when
        List<SearchingClubDto> result = clubService.searchingClub(request, "category");

        //then
        assertThat(result.size()).isEqualTo(10);

        assertThat(result).extracting("category")
                          .contains(Category.WALK);
    }

    @Test
    @DisplayName("검색 타입이 검색어일 경우 모임 이름 중 검색어가 포함된 모임 중에 사용자로부터 가까운 모임 10개를 조회한다")
    void searchingClubByWord() throws Exception {
        //given
        SearchingClubDto.SearchingRequest request = new SearchingClubDto.SearchingRequest();

        request.setSearchingWord("산책");
        request.setStartLatitude(23D);
        request.setStartLongitude(24D);

        //when
        List<SearchingClubDto> result = clubService.searchingClub(request, "word");

        //then
        assertThat(result.size()).isEqualTo(10);

        for (SearchingClubDto searchingClubDto : result) {
            assertThat(searchingClubDto.getTitle()).contains("산책");
        }
    }

    @Test
    @DisplayName("스케줄러가 실행될 경우 club의 상태가 END로 변경되어야한다")
    void convertStatusAvailableToEnd() throws Exception {
        //when
        List<Club> savedClubs = clubService.exceedTimeClub();

        //then
        for (Club savedClub : savedClubs) {
            assertThat(savedClub.getStatus()).isEqualTo(ClubStatus.END);
        }
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
        List<SearchingWithinRangeClubDto> result = clubService.searchingRangeClub(request);
        SearchingWithinRangeClubDto firstResult = result.get(0);

        //then
        assertAll(
                () -> assertTrue(firstResult.getClubLatitude() >= bottomRightLatitude &&
                                         firstResult.getClubLatitude() <= upperLeftLatitude),
                () -> assertTrue(firstResult.getClubLongitude() >= upperLeftLongitude &&
                                         firstResult.getClubLongitude() <= bottomRightLongitude)
        );
    }
}