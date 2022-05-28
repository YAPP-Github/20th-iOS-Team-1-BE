package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.EligibleBreed;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    public static class SearchingRequest{
        private String searchingWord;

        private Double latitude;

        private Double longitude;

        private int page;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchingResponse{
        private List<SearchingClubDto> searchingClubDto;
    }
}
