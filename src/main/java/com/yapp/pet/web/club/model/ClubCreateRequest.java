package com.yapp.pet.web.club.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ClubCreateRequest {

    @NotNull
    private String meetingPlace;

    @NotNull
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull
    private int maximumPeople;

    @NotNull
    private EligibleSex eligibleSex;

    @NotNull
    private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();

    @NotNull
    private Set<String> eligibleBreeds = new HashSet<>();

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
