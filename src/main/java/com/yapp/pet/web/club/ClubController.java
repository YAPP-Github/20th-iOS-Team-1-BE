package com.yapp.pet.web.club;

import com.yapp.pet.domain.club.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/clubs/search")
    public SearchingResponse searchingByWord(@ModelAttribute SearchingRequest request,
                                             @RequestParam("searchingType") String searchingType) {

        log.info("SearchingType = {}", searchingType);
        log.info("category = {}", request.getCategory());
        log.info("petSizeType = {}", request.getPetSizeType());
        log.info("eligibleSex = {}", request.getEligibleSex());
        log.info("EligibleBreed = {}", request.getEligibleBreed());

        return new SearchingResponse(clubService.searchingClub(request, searchingType));
    }

    @GetMapping("/clubs/search/range")
    public SearchingWithinRangeClubResponse searchingWithinRange(
            @ModelAttribute SearchingWithinRangeClubRequest request) {

        return new SearchingWithinRangeClubResponse(clubService.searchingRangeClub(request));
    }
}