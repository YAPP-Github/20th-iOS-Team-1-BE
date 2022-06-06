package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.web.club.model.SearchingClubDto;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;

public interface ClubRepositoryCustom {
    List<SearchingClubDto> searchClubByWord(SearchingRequest searchingRequest);

    List<SearchingClubDto> searchClubByCategory(SearchingRequest searchingRequest);

    List<Club> findExceedTimeClub();
}
