package com.yapp.pet.web.club;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.service.ClubQueryService;
import com.yapp.pet.domain.club.service.ClubService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.club.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<SearchingResponse>> searchingByWord(@Valid @ModelAttribute SearchingRequest request) {

        log.info("category = {}", request.getCategory());
        log.info("petSizeType = {}", request.getPetSizeType());
        log.info("eligibleSex = {}", request.getEligibleSex());
        log.info("EligibleBreed = {}", request.getEligibleBreed());

        return ResponseEntity.ok(clubQueryService.searchingClub(request));
    }

    @GetMapping("/clubs/search/range")
    public ResponseEntity<List<SearchingWithinRangeClubResponse>> searchingWithinRange(
            @Valid @ModelAttribute SearchingWithinRangeClubRequest request) {

        return ResponseEntity.ok(clubQueryService.searchingRangeClub(request));
    }

    @GetMapping("/clubs/search/simple/{club-id}")
    public SearchingSimpleClubResponse searchingSimpleInfo(@Valid @ModelAttribute SearchingSimpleClubRequest request,
                                                           @PathVariable("club-id") Long clubId) {

        return clubQueryService.searchingSimpleClub(request, clubId);
    }

    @GetMapping("/clubs")
    public ResponseEntity<ClubFindResponse> findClubsByCondition(
            @ModelAttribute ClubFindByConditionRequest request,
            @PageableDefault(size = 10, sort = "endDate", direction = ASC) Pageable pageable,
            @AuthAccount Account account){

        ClubFindResponse response = clubQueryService.findClubsByCondition(request, account, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/clubs/{club-id}")
    public ResponseEntity<ClubFindDetailResponse> findClubDetail(@PathVariable("club-id") Long clubId,
                                                                 @AuthAccount Account account){

        ClubFindDetailResponse response = clubQueryService.findClubDetail(clubId, account);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clubs/leave/{club-id}")
    public ResponseEntity<Long> leaveClub(@PathVariable("club-id") Long clubId,
                                          @AuthAccount Account account){

        Long accountId = clubService.leaveClub(clubId, account);

        return ResponseEntity.ok(accountId);
    }

    @PostMapping("/clubs")
    public ResponseEntity<Long> createClub(@AuthAccount Account account, @RequestBody ClubCreateRequest clubCreateRequest) {

        long savedId = clubService.createClub(account, clubCreateRequest);

        clubService.createClubDocument(savedId);

        return ResponseEntity.ok(savedId);
    }

    @DeleteMapping("/clubs/{club-id}")
    public ResponseEntity<Long> deleteClub(@PathVariable("club-id") Long clubId,
                                           @AuthAccount Account account){

        Long deletedClubId = clubService.deleteClub(clubId, account);

        return ResponseEntity.ok(deletedClubId);
    }

    @PostMapping("/clubs/participate/{club-id}")
    public ResponseEntity<ClubParticipateResponse> participateClub(@PathVariable("club-id") Long clubId,
                                                                   @AuthAccount Account loginAccount){

        ClubParticipateResponse response = clubService.participateClub(clubId, loginAccount);

        clubService.updateAccountClubDocument(response.getClubId());

        return ResponseEntity.ok(response);
    }
}