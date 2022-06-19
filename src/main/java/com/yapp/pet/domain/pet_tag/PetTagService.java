package com.yapp.pet.domain.pet_tag;

import com.yapp.pet.domain.pet.entity.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetTagService {

    private final PetTagRepository petTagRepository;

    public PetTag createPetTag(Pet pet, String name) {
        log.info("petTag name = {}", name);

        PetTag petTag = PetTag.of(pet, name);
        pet.addTag(petTag);

        return petTagRepository.save(petTag);
    }
}
