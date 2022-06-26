package com.yapp.pet.domain.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubFindCondition;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.global.mapper.ClubMapper;
import com.yapp.pet.web.club.model.ClubFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.web.club.model.ClubFindResponse.ClubInfo;
import static com.yapp.pet.web.club.model.ClubFindResponse.of;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubRequest;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubResponse;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubQueryService {

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public List<SearchingResponse> searchingClub(SearchingRequest searchingRequest, String SearchingType) {

        List<Club> savedClub;

        if (SearchingType.equals("word")) {
            savedClub = clubRepository.searchClubByWord(searchingRequest);
        } else{
            savedClub = clubRepository.searchClubByCategory(searchingRequest);
        }

        return savedClub.stream()
                        .map(SearchingResponse::new)
                        .map(dto -> dto.getDistanceBetweenAccountAndClub(searchingRequest.getStartLatitude(),
                                                                         searchingRequest.getStartLongitude()))
                        .sorted((dto1, dto2) -> {
                            return dto1.getDistance() - dto2.getDistance();
                        })
                        .collect(Collectors.toList());
    }

    public List<SearchingWithinRangeClubResponse> searchingRangeClub(SearchingWithinRangeClubRequest rangeRequest) {
        return clubRepository.searchClubByWithinRange(rangeRequest);
    }

    public List<Club> exceedTimeClub() {
        return clubRepository.findExceedTimeClub();
    }

    public SearchingSimpleClubResponse searchingSimpleClub(SearchingSimpleClubRequest simpleRequest, Long clubId) {
        Club savedClub = clubRepository.findById(clubId)
                                       .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 club"));

        return new SearchingSimpleClubResponse(savedClub, savedClub.getAccountClubs().size())
                .getDistanceBetweenAccountAndClub(simpleRequest.getUserLatitude(), simpleRequest.getUserLongitude());
    }

    public ClubFindResponse findClubsByCondition(Long cursorId, ClubFindCondition condition, Account account,
                                                 Pageable pageable){

        Page<ClubInfo> findClubInfos = clubRepository.findClubsByCondition(cursorId, condition, account, pageable)
                                                     .map(clubMapper::toInfo);

        ClubFindResponse response = ClubFindResponse.of(findClubInfos, false);

        if(findClubInfos.getTotalElements() == 0){
            response.setHasNotClub(true);
        }

        return response;
    }

}
