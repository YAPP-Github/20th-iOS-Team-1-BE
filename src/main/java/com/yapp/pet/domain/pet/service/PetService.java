package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.pet.entity.Age;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_image.PetImage;
import com.yapp.pet.domain.pet_image.PetImageService;
import com.yapp.pet.domain.pet_tag.PetTag;
import com.yapp.pet.domain.pet_tag.PetTagService;
import com.yapp.pet.web.pet.model.PetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.global.TogaetherConstants.YEAR_TO_MONTH;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetService {

    private final PetRepository petRepository;

    private final PetImageService petImageService;

    private final PetTagService petTagService;

    public long addPet(Account account, PetRequest petRequest) {

        PetImage petImage = null; //없을 수도 있다

        if (hasImageFile(petRequest.getImageFile())) {
            petImage = petImageService.create(petRequest.getImageFile());
        }

        Pet pet = Pet.builder()
                       .account(account)
                       .petImage(petImage)
                       .breed(petRequest.getBreed())
                       .name(petRequest.getName())
                       .age(calculateAge(petRequest.getYear(), petRequest.getMonth()))
                       .sex(petRequest.getSex())
                       .neutering(petRequest.isNeutering())
                       .sizeType(petRequest.getSizeType())
                       .build();

        petRequest.getTags()
                  .forEach(tag -> petTagService.createPetTag(pet, tag));

        petRepository.save(pet);

        return pet.getId();
    }

    private boolean hasImageFile(MultipartFile imageFile){
        return imageFile != null && !imageFile.isEmpty();
    }

    private Age calculateAge(int birthYear, int birthMonth) {
        int currentYear = ZonedDateTime.now().getYear();
        int currentMonth = ZonedDateTime.now().getMonth().getValue();

        int currentTotalMonth = YEAR_TO_MONTH * currentYear + currentMonth;
        int birthTotalMonth = YEAR_TO_MONTH * birthYear + birthMonth;

        if (currentTotalMonth - birthTotalMonth < 12) {
            return new Age((currentTotalMonth - birthTotalMonth) + "개월", birthYear, birthMonth);
        }

        return new Age((currentYear - birthYear) + "살", birthYear, birthMonth);
    }
}