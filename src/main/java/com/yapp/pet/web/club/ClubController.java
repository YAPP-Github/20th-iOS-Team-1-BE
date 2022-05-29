package com.yapp.pet.web.club;

import com.yapp.pet.domain.club.ClubService;
import com.yapp.pet.web.club.model.SearchingClubDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/clubs/search")
    public SearchingResponse searchingByWord(@ModelAttribute SearchingRequest request, @RequestParam("searchingType") String searchingType) {
        log.info("searchingWord = {}", request.getSearchingWord());
        log.info("userLatitude = {}", request.getLatitude());
        log.info("userLongitude = {}", request.getLongitude());
        log.info("startPage = {}", request.getPage());
        log.info("SearchingType = {}", searchingType);

        List<SearchingClubDto> searchingClubs = clubService.searchingClub(request.getSearchingWord(),
                                                                          request.getLatitude(),
                                                                          request.getLongitude(),
                                                                          request.getPage(), searchingType);

        return new SearchingResponse(searchingClubs);
    }
}