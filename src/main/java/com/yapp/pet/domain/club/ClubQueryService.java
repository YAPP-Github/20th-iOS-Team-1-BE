package com.yapp.pet.domain.club;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

@Service
@RequiredArgsConstructor
public class ClubQueryService {

    private final ClubRepository clubRepository;

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
}
