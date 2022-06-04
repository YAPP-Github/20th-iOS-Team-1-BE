package com.yapp.pet.domain.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.entity.Address;
import com.yapp.pet.domain.accountclub.entity.AccountClub;
import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.EligibleBreed;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.club.model.SearchingClubDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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

}