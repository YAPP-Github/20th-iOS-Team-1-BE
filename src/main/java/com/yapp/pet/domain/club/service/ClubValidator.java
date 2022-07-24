package com.yapp.pet.domain.club.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.web.club.model.ClubParticipateResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yapp.pet.web.club.model.ClubParticipateRejectReason.*;

@Component
public class ClubValidator {

    public ClubParticipateResponse participationValidate(Club club, List<Pet> pets, Account account){
        if (hasNotPet(pets)) {
            return ClubParticipateResponse.of(false, HAS_NOT_PET);
        }

        if (isFull(club)) {
            return ClubParticipateResponse.of(false, FULL);
        }

        if (!isEligibleSex(account, club)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_SEX);
        }

        if (!isEligiblePetSizeType(club, pets)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_PET_SIZE_TYPE);
        }

        if (!isEligibleBreeds(club, pets)) {
            return ClubParticipateResponse.of(false, NOT_ELIGIBLE_BREEDS);
        }

        return ClubParticipateResponse.of(true, null);
    }

    private boolean hasNotPet(List<Pet> findPets) {
        return findPets == null || findPets.isEmpty();
    }

    private boolean isFull(Club club){
        return club.getStatus().equals(ClubStatus.PERSONNEL_FULL);
    }

    private boolean isEligibleSex(Account loginAccount, Club club) {
        AccountSex accountSex = loginAccount.getSex();
        EligibleSex eligibleSex = club.getEligibleSex();

        if (eligibleSex == EligibleSex.ALL) {
            return true;
        }

        if (eligibleSex == EligibleSex.MAN && accountSex == AccountSex.MAN) {
            return true;
        }

        if (eligibleSex == EligibleSex.WOMAN && accountSex == AccountSex.WOMAN) {
            return true;
        }

        return false;
    }

    private boolean isEligiblePetSizeType(Club club, List<Pet> findPets) {
        if (club.getEligiblePetSizeTypes().contains(PetSizeType.ALL)) {
            return true;
        }

        return findPets.stream()
                .anyMatch(pet -> club.getEligiblePetSizeTypes().contains(pet.getSizeType()));
    }

    private boolean isEligibleBreeds(Club club, List<Pet> findPets) {
        if (club.getEligibleBreeds() == null || club.getEligibleBreeds().isEmpty()) {
            return true;
        }

        return findPets.stream()
                .anyMatch(pet -> club.getEligibleBreeds().contains(pet.getBreed()));
    }

}
