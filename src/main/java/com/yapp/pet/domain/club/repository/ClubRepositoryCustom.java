package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.web.club.model.SearchingClubDto;
import com.yapp.pet.web.club.model.SearchingWithinRangeClubDto;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

public interface ClubRepositoryCustom {
    List<SearchingClubDto> searchClubByWord(SearchingRequest searchingRequest);

    List<SearchingClubDto> searchClubByCategory(SearchingRequest searchingRequest);

    List<SearchingWithinRangeClubDto> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest);

    SearchingSimpleClubResponse searchSimpleClubById(SearchingSimpleClubRequest simpleRequest, Long clubId);

    List<Club> findExceedTimeClub();
}
