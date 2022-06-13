package com.yapp.pet.domain.club;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.web.club.model.SearchingClubDto;
import com.yapp.pet.web.club.model.SearchingWithinRangeClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public List<SearchingClubDto> searchingClub(SearchingRequest searchingRequest, String SearchingType) {
        if (SearchingType.equals("word")) {
            return clubRepository.searchClubByWord(searchingRequest);
        }

        return clubRepository.searchClubByCategory(searchingRequest);
    }

    public List<SearchingWithinRangeClubDto> searchingRangeClub(SearchingWithinRangeClubRequest rangeRequest) {
        return clubRepository.searchClubByWithinRange(rangeRequest);
    }

    public List<Club> exceedTimeClub() {
        return clubRepository.findExceedTimeClub();
    }

    public SearchingSimpleClubResponse searchingSimpleClub(SearchingSimpleClubRequest simpleRequest, Long clubId) {
        return clubRepository.searchSimpleClubById(simpleRequest, clubId);
    }
}
