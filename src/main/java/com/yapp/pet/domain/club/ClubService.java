package com.yapp.pet.domain.club;

import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.web.club.model.SearchingClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;

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
}
