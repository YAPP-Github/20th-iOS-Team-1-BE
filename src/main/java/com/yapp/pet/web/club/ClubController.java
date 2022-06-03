package com.yapp.pet.web.club;

import com.yapp.pet.domain.club.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yapp.pet.web.club.model.SearchingClubDto.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Tag(name = "모임 API", description = "모임 검색 및 필터링을 진행합니다")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/clubs/search")
    @Operation(summary = "모임 검색 및 필터링", tags = "모임 API",
            description = "Query String을 통해 데이터를 요청받고, 조건에 맞는 모임을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = SearchingResponse.class)))
    public SearchingResponse searchingByWord(@Parameter(description = "SearchingRequest")
                                             @ModelAttribute SearchingRequest request,
                                             @Parameter(description = "검색어를 통한 검색은 word, 카테고리 검색은 category")
                                             @RequestParam("searchingType") String searchingType) {

        log.info("SearchingType = {}", searchingType);
        log.info("category = {}", request.getCategory());
        log.info("petSizeType = {}", request.getPetSizeType());
        log.info("eligibleSex = {}", request.getEligibleSex());
        log.info("EligibleBreed = {}", request.getEligibleBreed());

        return new SearchingResponse(clubService.searchingClub(request, searchingType));
    }
}