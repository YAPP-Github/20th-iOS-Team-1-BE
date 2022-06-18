package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

public interface ClubRepositoryCustom {
    List<Club> searchClubByWord(SearchingRequest searchingRequest);

    List<Club> searchClubByCategory(SearchingRequest searchingRequest);

    List<SearchingWithinRangeClubResponse> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest);

    List<Club> findExceedTimeClub();
}
