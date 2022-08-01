package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_image.PetImageService;
import com.yapp.pet.domain.pet_tag.PetTagService;
import com.yapp.pet.global.exception.pet.NotFoundPetException;
import com.yapp.pet.global.mapper.PetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@DisplayName("PetService Unit Test")
public class PetServiceTest {

    PetRepository petRepository;

    PetImageService petImageService;

    PetTagService petTagService;

    PetMapper petMapper;

    PetService petService;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        petImageService = mock(PetImageService.class);
        petTagService = mock(PetTagService.class);
        petMapper = mock(PetMapper.class);

        petService = new PetService(petRepository, petImageService, petTagService, petMapper);
    }

    @Test
    @DisplayName("deletePetInfo() : 펫을 삭제할 경우, 삭제할 펫이 없다면 NotFoundPetException를 반환한다")
    void testDeletePetInfo_NotFoundPetExcetion() throws Exception {
        //when
        assertThrows(NotFoundPetException.class,
                     () -> petService.deletePetInfo(1L));
    }
}
