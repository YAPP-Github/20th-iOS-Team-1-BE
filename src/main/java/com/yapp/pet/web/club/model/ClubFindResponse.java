package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubFindResponse {

    private Boolean hasNotClub;

    private Page<ClubInfo> clubInfos;

    public static ClubFindResponse of(Page<ClubInfo> findClubInfos, Boolean hasNotClub){
        return new ClubFindResponse(hasNotClub, findClubInfos);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ClubInfo {

        private Long clubId;
        private String title;
        private Category category;
        private String meetingPlace;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private Set<String> eligibleBreeds = new HashSet<>();
        private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();
        private EligibleSex eligibleSex;
        private int maximumPeople;
        private int participants;

    }

}
