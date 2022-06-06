package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleBreed;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SearchingClubDto {

    private Category category;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();
    private Set<EligibleBreed> eligibleBreeds = new HashSet<>();
    private EligibleSex eligibleSex;
    private int maximumPeople;
    private int participants;
    private String leaderName;

    private String imagePath;
    private Double latitude;
    private Double longitude;
    private String meetingPlace;

    @Builder
    public SearchingClubDto(Club club, String leaderName, int participants) {
        this.category = club.getCategory();
        this.title = club.getTitle();
        this.startDate = club.getStartDate();
        this.endDate = club.getEndDate();
        this.eligiblePetSizeTypes = club.getEligiblePetSizeTypes();
        this.eligibleBreeds = club.getEligibleBreeds();
        this.eligibleSex = club.getEligibleSex();
        this.maximumPeople = club.getMaximumPeople();
        this.latitude = club.getLatitude();
        this.longitude = club.getLongitude();
        this.participants = participants;
        this.leaderName = leaderName;
        this.meetingPlace = club.getMeetingPlace();
    }

    @Getter
    @Setter
    @Schema(description = "모임 조회 및 필터링 요청 API")
    public static class SearchingRequest{
        @Schema(title = "검색어", description = "카테고리를 통한 검색일 경우, 해당 값은 null")
        private String searchingWord;

        private Category category;

        private EligibleBreed eligibleBreed;

        private PetSizeType petSizeType;

        private EligibleSex eligibleSex;

        @Schema(title = "최소 참여 인원 필터링", description = "3명 이하일 경우 해당 값은 0으로 전송")
        private Integer participateMin;

        @Schema(title = "최대 참여 인원 필터링")
        private Integer participateMax;

        @Schema(description = "몇 번째 게시물인지 나타낸다")
        private int page;

        @Schema(description = "사용자 위도")
        private Double startLatitude;

        @Schema(description = "사용자 경도")
        private Double startLongitude;

        private ClubStatus status;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchingResponse{
        private List<SearchingClubDto> searchingClubDto;
    }
}
