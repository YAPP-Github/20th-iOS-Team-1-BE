package com.yapp.pet.web.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.service.ClubQueryService;
import com.yapp.pet.domain.club.repository.ClubFindCondition;
import com.yapp.pet.domain.club.service.ClubService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.club.model.ClubCreateRequest;
import com.yapp.pet.web.club.model.ClubFindDetailResponse;
import com.yapp.pet.web.club.model.ClubFindResponse;
import com.yapp.pet.web.club.model.ClubParticipateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubRequest;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubResponse;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ClubController {

    private final ClubQueryService clubQueryService;
    private final ClubService clubService;

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

    @GetMapping("/clubs")
    public ResponseEntity<ClubFindResponse> findClubsByCondition(
            @RequestParam(value = "cursor-id", required = false) Long cursorId,
            @RequestParam(value = "condition", required = false) ClubFindCondition condition,
            @PageableDefault(size = 10, sort = "endDate", direction = ASC) Pageable pageable,
            @AuthAccount Account account){

        ClubFindResponse response;

        try {
            response = clubQueryService.findClubsByCondition(cursorId, condition, account, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/clubs/{club-id}")
    public ResponseEntity<ClubFindDetailResponse> findClubDetail(@PathVariable("club-id") Long clubId,
                                                                 @AuthAccount Account account){

        ClubFindDetailResponse response;

        try {
            response = clubQueryService.findClubDetail(clubId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clubs/leave/{club-id}")
    public ResponseEntity<Void> leaveClub(@PathVariable("club-id") Long clubId,
                                          @AuthAccount Account account){

        try {
            clubService.leaveClub(clubId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/clubs")
    public long createClub(@AuthAccount Account account, ClubCreateRequest clubCreateRequest) {

        long savedId = 0L;

        try {
            savedId = clubService.create(account, clubCreateRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return savedId;
    }

    @DeleteMapping("/clubs/{club-id}")
    public ResponseEntity<Void> deleteClub(@PathVariable("club-id") Long clubId,
                                           @AuthAccount Account account){

        try {
            clubService.deleteClub(clubId, account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/clubs/participate/{club-id}")
    public ResponseEntity<ClubParticipateResponse> participateClub(@PathVariable("club-id") Long clubId,
                                                                   @AuthAccount Account loginAccount){

        ClubParticipateResponse response;

        try {
            response = clubService.isEligibleClub(clubId, loginAccount);

            if (!response.isEligible()) {
                return ResponseEntity.badRequest().body(response);
            }

            clubService.participateClub(clubId, loginAccount);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

}