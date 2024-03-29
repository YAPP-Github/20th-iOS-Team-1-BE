package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.pet.entity.Age;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_image.PetImage;
import com.yapp.pet.domain.pet_image.PetImageService;
import com.yapp.pet.domain.pet_tag.PetTag;
import com.yapp.pet.domain.pet_tag.PetTagService;
import com.yapp.pet.global.exception.pet.NotFoundPetException;
import com.yapp.pet.global.mapper.PetMapper;
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

    private final PetMapper petMapper;

    public Long addPet(Account account, PetRequest petRequest) {

        PetImage petImage = null;

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


        petRepository.save(pet);

        petRequest.getTags()
                  .forEach(tag -> petTagService.createPetTag(pet, tag));

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

    public Long deletePetInfo(long petId) {
        Pet savedPet = petRepository.findById(petId)
                                    .orElseThrow(NotFoundPetException::new);

        if (savedPet.getPetImage() != null) {
            petImageService.delete(savedPet);
        }

        petRepository.delete(savedPet);

        return savedPet.getId();
    }

    public void deleteAllPetInfo(Account account) {

        List<Pet> pets = petRepository.findPetsByAccountId(account.getId());

        pets.stream()
            .map(p -> p.getTags()
                       .stream()
                       .map(PetTag::getId)
                       .collect(Collectors.toList()))
            .forEach(petTagService::deletePetTag);

        petRepository.deletePetByIds(pets.stream()
                                         .map(Pet::getId)
                                         .collect(Collectors.toList()));
    }

    public Long updatePetInfo(long petId, PetRequest request) {
        Pet savedPet = petRepository.findById(petId)
                                    .orElseThrow(NotFoundPetException::new);

        MultipartFile imageFile = request.getImageFile();

        if (hasImageFile(imageFile)) {
            PetImage createdPetImage = petImageService.create(imageFile);
            savedPet.addImage(createdPetImage);
        }

        updateTags(savedPet, request.getTags());

        Pet updatePet = petMapper.toEntity(request);

        updatePet.updateAge(calculateAge(updatePet.getAge().getBirthYear(), updatePet.getAge().getBirthMonth()));

        savedPet.update(updatePet);

        return savedPet.getId();
    }

    private void updateTags(Pet pet, List<String> tags) {
        pet.getTags().clear();
        tags.forEach(tag -> petTagService.createPetTag(pet, tag));
    }
}