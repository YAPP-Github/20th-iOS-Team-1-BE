package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.global.util.DistanceUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SearchingSimpleClubDto {

    @Getter
    @Setter
    public static class SearchingSimpleClubRequest {

        @NotNull
        private Double userLatitude;

        @NotNull
        private Double userLongitude;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchingSimpleClubResponse {
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

        public SearchingSimpleClubResponse(Club club, int participants) {
            this.category = club.getCategory();
            this.title = club.getTitle();
            this.startDate = club.getStartDate();
            this.endDate = club.getEndDate();
            this.eligiblePetSizeTypes = club.getEligiblePetSizeTypes();
            this.eligibleBreeds = club.getEligibleBreeds();
            this.eligibleSex = club.getEligibleSex();
            this.maximumPeople = club.getMaximumPeople();
            this.participants = participants;
            this.clubStatus = club.getStatus();
            this.latitude = club.getLatitude();
            this.longitude = club.getLongitude();
            this.meetingPlace = club.getMeetingPlace();
        }

        public SearchingSimpleClubResponse getDistanceBetweenAccountAndClub(Double userLatitude, Double userLongitude) {
            distance = (int) DistanceUtil.getDistanceBetweenUserAndClub(userLatitude, latitude,
                                                                        userLongitude, longitude);
            return this;
        }
    }
}