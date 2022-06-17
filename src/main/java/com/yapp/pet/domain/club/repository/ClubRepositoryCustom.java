package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.web.club.model.SearchingWithinRangeClubDto;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

public interface ClubRepositoryCustom {
    List<Club> searchClubByWord(SearchingRequest searchingRequest);

    List<Club> searchClubByCategory(SearchingRequest searchingRequest);

    List<SearchingWithinRangeClubResponse> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest);

    SearchingSimpleClubResponse searchSimpleClubById(SearchingSimpleClubRequest simpleRequest, Long clubId);

    List<Club> findExceedTimeClub();
}
