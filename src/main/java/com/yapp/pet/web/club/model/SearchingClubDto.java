package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.document.ClubDocument;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.global.util.DistanceUtil;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SearchingClubDto {

    @Getter
    @Setter
    public static class SearchingRequest{

        @NotNull
        private String searchingWord;

        @NotNull
        private Category category;

        @NotNull
        private String eligibleBreed;

        @NotNull
        private PetSizeType petSizeType;

        @NotNull
        private EligibleSex eligibleSex;

        @NotNull
        private Integer participateMin;

        @NotNull
        private Integer participateMax;

        @NotNull
        private int page;

        @NotNull
        private Double startLatitude;

        @NotNull
        private Double startLongitude;

        @NotNull
        private ClubStatus status;
    }

    @Getter
    public static class SearchingResponse{
        private Category category;
        private String title;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();
        private Set<String> eligibleBreeds = new HashSet<>();
        private EligibleSex eligibleSex;
        private int maximumPeople;
        private int participants;
        private Double latitude;
        private Double longitude;
        private String meetingPlace;
        private int distance;
        private ClubStatus clubStatus;

        public SearchingResponse(ClubDocument clubDocument) {
            this.category = clubDocument.getCategory();
            this.title = clubDocument.getTitle();
            this.startDate = clubDocument.getStartDate();
            this.endDate = clubDocument.getEndDate();
            this.eligiblePetSizeTypes = clubDocument.getEligiblePetSizeTypes();
            this.eligibleBreeds = clubDocument.getEligibleBreeds();
            this.eligibleSex = clubDocument.getEligibleSex();
            this.maximumPeople = clubDocument.getMaximumPeople();
            this.latitude = clubDocument.getLatitude();
            this.longitude = clubDocument.getLongitude();
            this.participants = clubDocument.getAccountClubs().size();
            this.meetingPlace = clubDocument.getMeetingPlace();
            this.clubStatus = clubDocument.getStatus();
        }

        public SearchingResponse getDistanceBetweenAccountAndClub(Double userLatitude, Double userLongitude) {
            distance = (int) DistanceUtil.getDistanceBetweenUserAndClub(userLatitude, latitude,
                                                                        userLongitude, longitude);
            return this;
        }
    }
}
