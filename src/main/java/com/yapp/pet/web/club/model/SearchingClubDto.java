package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.*;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.*;

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

        private Category category;

        private EligibleBreed eligibleBreed;

        private PetSizeType petSizeType;

        private EligibleSex eligibleSex;

        private Integer participateMin;

        private Integer participateMax;

        private int page;

        private Double startLatitude;

        private Double startLongitude;

        private ClubStatus status;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchingResponse{
        private List<SearchingClubDto> searchingClubDto;
    }
}
