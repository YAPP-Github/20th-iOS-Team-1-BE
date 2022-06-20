package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.pet.entity.Age;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_image.PetImage;
import com.yapp.pet.domain.pet_image.PetImageService;
import com.yapp.pet.domain.pet_tag.PetTagService;
import com.yapp.pet.global.mapper.PetMapper;
import com.yapp.pet.web.pet.model.PetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;

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

    public void deletePetInfo(long petId) {
        Pet savedPet = petRepository.findById(petId)
                                    .orElseThrow(() -> new IllegalArgumentException("존재하는 펫이 없습니다"));

        if (savedPet.getPetImage() != null) {
            petImageService.delete(savedPet);
        }

        petRepository.delete(savedPet);
    }

    public void updatePetInfo(long petId, PetRequest request) {
        Pet savedPet = petRepository.findById(petId)
                                    .orElseThrow(() -> new IllegalArgumentException("존재하는 펫이 없습니다"));

        PetImage petImage = savedPet.getPetImage();
        MultipartFile imageFile = request.getImageFile();

        if (hasImageFile(imageFile)) {
            PetImage createdPetImage = petImageService.create(imageFile);
            savedPet.addImage(createdPetImage);
        }

        updateTags(savedPet, request.getTags());

        Pet updatePet = petMapper.toEntity(request);

        log.info("age = {}", updatePet.getAge());
        log.info("sex = {}", updatePet.getSex());
        log.info("sizeType = {}", updatePet.getSizeType());
        log.info("petImage = {}", updatePet.getPetImage());

        updatePet.updateAge(calculateAge(updatePet.getAge().getBirthYear(), updatePet.getAge().getBirthMonth()));

        savedPet.update(updatePet);
    }

    private void updateTags(Pet pet, List<String> tags) {
        pet.getTags().clear();
        tags.forEach(tag -> petTagService.createPetTag(pet, tag));
    }
}