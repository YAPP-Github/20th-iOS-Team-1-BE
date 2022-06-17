package com.yapp.pet.web.club;

import com.yapp.pet.domain.club.ClubQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ClubController {

    private final ClubQueryService clubQueryService;

    @GetMapping("/clubs/search")
    public ResponseEntity<List<SearchingResponse>> searchingByWord(@ModelAttribute SearchingRequest request,
                                                                   @RequestParam("searchingType") String searchingType) {

        log.info("SearchingType = {}", searchingType);
        log.info("category = {}", request.getCategory());
        log.info("petSizeType = {}", request.getPetSizeType());
        log.info("eligibleSex = {}", request.getEligibleSex());
        log.info("EligibleBreed = {}", request.getEligibleBreed());

        return ResponseEntity.ok(clubQueryService.searchingClub(request, searchingType));
    }

    @GetMapping("/clubs/search/range")
    public ResponseEntity<List<SearchingWithinRangeClubResponse>> searchingWithinRange(
            @ModelAttribute SearchingWithinRangeClubRequest request) {

        return ResponseEntity.ok(clubQueryService.searchingRangeClub(request));
    }

    @GetMapping("/clubs/search/simple/{club-id}")
    public SearchingSimpleClubResponse searchingSimpleInfo(SearchingSimpleClubRequest request,
                                                           @PathVariable("club-id") Long clubId) {
        return clubQueryService.searchingSimpleClub(request, clubId);
    }
}