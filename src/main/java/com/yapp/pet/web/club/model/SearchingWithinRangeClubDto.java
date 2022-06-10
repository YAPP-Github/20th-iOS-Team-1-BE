package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.ClubStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchingWithinRangeClubDto {

    private Long clubId;

    private ClubStatus clubStatus;

    private Double clubLatitude;

    private Double clubLongitude;

    @Builder
    public SearchingWithinRangeClubDto(Long clubId, ClubStatus clubStatus, Double clubLatitude, Double clubLongitude) {
        this.clubId = clubId;
        this.clubStatus = clubStatus;
        this.clubLatitude = clubLatitude;
        this.clubLongitude = clubLongitude;
    }

    @Getter
    @Setter
    public static class SearchingWithinRangeClubRequest{
        private Double upperLeftLatitude;
        private Double upperLeftLongitude;
        private Double bottomRightLatitude;
        private Double bottomRightLongitude;

        @Builder
        public SearchingWithinRangeClubRequest(Double upperLeftLatitude, Double upperLeftLongitude,
                                               Double bottomRightLatitude,
                                               Double bottomRightLongitude) {
            this.upperLeftLatitude = upperLeftLatitude;
            this.upperLeftLongitude = upperLeftLongitude;
            this.bottomRightLatitude = bottomRightLatitude;
            this.bottomRightLongitude = bottomRightLongitude;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class SearchingWithinRangeClubResponse{
        List<SearchingWithinRangeClubDto> searchingWithinRangeClubDto;
    }
}
