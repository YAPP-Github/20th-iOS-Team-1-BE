package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

public interface ClubRepositoryCustom {
    List<Club> searchClubByWord(SearchingRequest searchingRequest);

    List<Club> searchClubByCategory(SearchingRequest searchingRequest);

    List<SearchingWithinRangeClubResponse> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest);

    List<Club> findExceedTimeClub();

    Page<Club> findClubsByCondition(Long cursorId, ZonedDateTime cursorEndDate, ClubFindCondition condition, Account account, Pageable pageable);

    Optional<Club> findClubDetailById(Long clubId);
}
