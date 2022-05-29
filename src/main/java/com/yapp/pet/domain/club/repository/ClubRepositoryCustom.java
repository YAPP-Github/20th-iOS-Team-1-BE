package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.web.club.model.SearchingClubDto;

import java.util.List;

public interface ClubRepositoryCustom {
    List<SearchingClubDto> searchClubByWord(String searchingWord, Double startLatitude, Double endLongitude, int page);

    List<SearchingClubDto> searchClubByCategory(Category category, Double startLatitude, Double endLongitude, int page);
}
