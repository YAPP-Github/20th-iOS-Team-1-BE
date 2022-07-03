package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ClubCreateRequest {

    private String meetingPlace;

    private Category category;

    private String title;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private int maximumPeople;

    private EligibleSex eligibleSex;

    private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();

    private Set<String> eligibleBreeds = new HashSet<>();

    private Double latitude;

    private Double longitude;
}
