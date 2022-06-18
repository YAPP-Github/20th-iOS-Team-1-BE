package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.common.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchingWithinRangeClubDto {

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
    @Setter
    @NoArgsConstructor
    public static class SearchingWithinRangeClubResponse {

        private Long clubId;
        private Category category;
        private Double clubLatitude;
        private Double clubLongitude;

        public SearchingWithinRangeClubResponse(Long clubId, Category category, Double clubLatitude, Double clubLongitude) {
            this.clubId = clubId;
            this.category = category;
            this.clubLatitude = clubLatitude;
            this.clubLongitude = clubLongitude;
        }
    }
}
