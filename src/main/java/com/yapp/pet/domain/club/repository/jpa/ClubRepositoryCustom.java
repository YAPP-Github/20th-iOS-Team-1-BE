package com.yapp.pet.domain.club.repository.jpa;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubFindCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

public interface ClubRepositoryCustom {

    List<SearchingWithinRangeClubResponse> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest);

    List<Club> findExceedTimeClub();

    Page<Club> findClubsByCondition(String customCursor, ClubFindCondition condition, Account account, Pageable pageable);

    Optional<Club> findClubDetailById(Long clubId);
}
