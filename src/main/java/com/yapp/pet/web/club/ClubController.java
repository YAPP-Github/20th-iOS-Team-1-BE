package com.yapp.pet.web.club;

import com.yapp.pet.domain.club.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;

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
}