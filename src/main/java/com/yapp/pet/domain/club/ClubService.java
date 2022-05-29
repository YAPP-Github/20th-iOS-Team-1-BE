package com.yapp.pet.domain.club;

import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.web.club.model.SearchingClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public List<SearchingClubDto> searchingClub(String searchingWord, Double userLatitude, Double userLongitude, int page, String SearchingType) {
        if (SearchingType.equals("word")) {
            return clubRepository.searchClubByWord(searchingWord, userLatitude, userLongitude, page);
        }

        return clubRepository.searchClubByCategory(Category.valueOf(searchingWord), userLatitude, userLongitude, page);
    }
}
