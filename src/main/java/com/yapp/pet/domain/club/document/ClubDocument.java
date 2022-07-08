package com.yapp.pet.domain.club.document;

import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "clubs")
public class ClubDocument {

    @Id
    private Long id;

    private List<AccountClubDocument> accountClubs = new ArrayList<>();

    private String title;

    private String description;

    private Category category;

    private String meetingPlace;

    private ClubStatus status;

    private int maximumPeople;

    private EligibleSex eligibleSex;

    private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();

    private Set<String> eligibleBreeds = new HashSet<>();

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Double latitude;

    private Double longitude;

    private ClubDocument(Club club) {
        id = club.getId();
        title = club.getTitle();
        accountClubs = club.getAccountClubs().stream()
                           .map(ac -> AccountClubDocument.of(ac.getClub().getId(), ac.getAccount().getId()))
                           .collect(Collectors.toList());
        description = club.getDescription();
        category = club.getCategory();
        meetingPlace = club.getMeetingPlace();
        status = club.getStatus();
        maximumPeople = club.getMaximumPeople();
        eligibleSex = club.getEligibleSex();
        eligiblePetSizeTypes = club.getEligiblePetSizeTypes();
        eligibleBreeds = club.getEligibleBreeds();
        startDate = club.getStartDate();
        endDate = club.getEndDate();
        latitude = club.getLatitude();
        longitude = club.getLongitude();
    }

    public static ClubDocument of(Club club) {
        return new ClubDocument(club);
    }

    @PersistenceConstructor
    public ClubDocument(Long id, List<AccountClubDocument> accountClubs, String title, String description,
                        Category category, String meetingPlace, ClubStatus status, int maximumPeople,
                        EligibleSex eligibleSex, Set<PetSizeType> eligiblePetSizeTypes, Set<String> eligibleBreeds,
                        ZonedDateTime startDate, ZonedDateTime endDate, Double latitude, Double longitude) {
        this.id = id;
        this.accountClubs = accountClubs;
        this.title = title;
        this.description = description;
        this.category = category;
        this.meetingPlace = meetingPlace;
        this.status = status;
        this.maximumPeople = maximumPeople;
        this.eligibleSex = eligibleSex;
        this.eligiblePetSizeTypes = eligiblePetSizeTypes;
        this.eligibleBreeds = eligibleBreeds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateAccountClubDocument(AccountClub accountClub) {
        accountClubs.add(AccountClubDocument.of((accountClub.getClub().getId()),
                                                accountClub.getAccount().getId()));
    }
}
