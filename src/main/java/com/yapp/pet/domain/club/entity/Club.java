package com.yapp.pet.domain.club.entity;

import com.yapp.pet.domain.accountclub.entity.AccountClub;
import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    private List<AccountClub> accountClubs = new ArrayList<>();

    @Column(nullable = false, length = 70)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String meetingPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubStatus status;

    @Column(nullable = false)
    private int maximumPeople;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EligibleSex eligibleSex;

    @Column(nullable = false)
    @ElementCollection
    @CollectionTable(name = "eligible_pet_size_types", joinColumns = @JoinColumn(name = "club_id"))
    private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();

    @Column(nullable = false)
    @ElementCollection
    @CollectionTable(name = "eligible_breeds", joinColumns = @JoinColumn(name = "club_id"))
    private Set<EligibleBreed> eligibleBreeds = new HashSet<>();

    @Column(nullable = false)
    private ZonedDateTime startDate;

    @Column(nullable = false)
    private ZonedDateTime endDate;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    public Club(String title, String description,
                Category category, String meetingPlace, int maximumPeople, EligibleSex eligibleSex,
                Set<PetSizeType> eligiblePetSizeTypes, Set<EligibleBreed> eligibleBreeds,
                ZonedDateTime startDate, ZonedDateTime endDate, Double latitude, Double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.meetingPlace = meetingPlace;
        this.status = ClubStatus.AVAILABLE;
        this.maximumPeople = maximumPeople;
        this.eligibleSex = eligibleSex;
        this.eligiblePetSizeTypes = eligiblePetSizeTypes;
        this.eligibleBreeds = eligibleBreeds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
