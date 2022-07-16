package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.document.ClubDocument;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.repository.ClubFindCondition;
import com.yapp.pet.domain.club.repository.ClubRepository;
import com.yapp.pet.domain.club.repository.ClubSearchRepository;
import com.yapp.pet.domain.comment.service.CommentQueryService;
import com.yapp.pet.global.mapper.ClubMapper;
import com.yapp.pet.web.club.model.ClubFindDetailResponse;
import com.yapp.pet.web.club.model.ClubFindResponse;
import com.yapp.pet.web.comment.model.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.web.club.model.ClubFindDetailResponse.*;
import static com.yapp.pet.web.club.model.ClubFindResponse.ClubInfo;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingResponse;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubRequest;
import static com.yapp.pet.web.club.model.SearchingSimpleClubDto.SearchingSimpleClubResponse;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubQueryService {

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    private final CommentQueryService commentQueryService;

    private final ClubSearchRepository clubSearchRepository;

    public List<SearchingResponse> searchingClub(SearchingRequest searchingRequest) {

        List<ClubDocument> savedClubDocument = clubSearchRepository.findByTitleCondition(searchingRequest);

        return savedClubDocument.stream()
                                .filter(document -> isParticipateBetweenMaxAndMin(searchingRequest.getParticipateMax(),
                                                                                  searchingRequest.getParticipateMin(),
                                                                                  document.getAccountClubs().size()))
                                .map(SearchingResponse::new)
                                .map(dto -> dto.getDistanceBetweenAccountAndClub(searchingRequest.getStartLatitude(),
                                                                                 searchingRequest.getStartLongitude()))
                                .sorted((dto1, dto2) -> {
                                    return dto1.getDistance() - dto2.getDistance();
                                })
                                .collect(Collectors.toList());
    }

    public boolean isParticipateBetweenMaxAndMin(Integer max, Integer min, int target) {
        if(max == null || min == null) return true;

        return target >= min && target <= max;
    }

    public List<SearchingWithinRangeClubResponse> searchingRangeClub(SearchingWithinRangeClubRequest rangeRequest) {
        return clubRepository.searchClubByWithinRange(rangeRequest);
    }

    public List<Club> exceedTimeClub() {
        return clubRepository.findExceedTimeClub();
    }

    public SearchingSimpleClubResponse searchingSimpleClub(SearchingSimpleClubRequest simpleRequest, Long clubId) {
        Club savedClub = findClubById(clubId);

        return new SearchingSimpleClubResponse(savedClub, savedClub.getAccountClubs().size())
                .getDistanceBetweenAccountAndClub(simpleRequest.getUserLatitude(), simpleRequest.getUserLongitude());
    }

    public Club findClubById(long clubId) {
        return clubRepository.findById(clubId)
                             .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 club"));
    }

    public ClubFindResponse findClubsByCondition(Long cursorId, LocalDateTime cursorEndDate,
                                                 ClubFindCondition condition, Account account, Pageable pageable){

        String customCursor = generateCustomCursor(cursorEndDate, cursorId);

        Page<ClubInfo> findClubInfos
                = clubRepository.findClubsByCondition(
                        customCursor, condition,
                        account, pageable
                )
                .map(clubMapper::toInfo);

        ClubFindResponse response = ClubFindResponse.of(findClubInfos, false);

        if(findClubInfos.getTotalElements() == 0){
            response.setHasNotClub(true);
        }

        return response;
    }

    private String generateCustomCursor(LocalDateTime cursorEndDate, Long cursorId){
        if (cursorEndDate == null && cursorId == null) {
            return null;
        }

        cursorEndDate = cursorEndDate.minusHours(9);

        String customCursorEndDate;
        String customCursorId;

        customCursorEndDate = cursorEndDate.toString()
                                                  .replaceAll("T", "")
                                                  .replaceAll("-", "")
                                                  .replaceAll(":", "") + "00";

        customCursorEndDate = String.format("%1$" + 20 + "s", customCursorEndDate)
                                    .replace(' ', '0');

        customCursorId = String.format("%1$" + 10 + "s", cursorId)
                               .replace(' ', '0');

        return customCursorEndDate + customCursorId;
    }

    public ClubFindDetailResponse findClubDetail(Long clubId, Account loginAccount) {
        Club findClub = clubRepository.findClubDetailById(clubId).orElseThrow(EntityNotFoundException::new);
        ClubDetailInfo clubDetailInfo = clubMapper.toDetailInfo(findClub);

        List<CommentResponse> findComments = commentQueryService.findComment(clubId, loginAccount);

        return ClubFindDetailResponse.builder()
                .accountClubs(findClub.getAccountClubs())
                .clubDetailInfo(clubDetailInfo)
                .loginAccount(loginAccount)
                .commentInfos(findComments)
                .build();
    }
}