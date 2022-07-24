package com.yapp.pet.domain.pet_tag;

import com.yapp.pet.domain.pet.entity.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetTagService {

    private final PetTagRepository petTagRepository;

    public void createPetTag(Pet pet, String name) {

        PetTag petTag = PetTag.of(pet, name);
        pet.addTag(petTag);
    }

    public void deletePetTag(List<Long> petTagIds) {
        petTagRepository.deletePetTagByPetIds(petTagIds);
    }
}
