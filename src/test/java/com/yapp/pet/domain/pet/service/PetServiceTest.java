package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.entity.PetSex;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_tag.PetTagRepository;
import com.yapp.pet.web.pet.model.PetRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
class PetServiceTest {

    @Autowired
    PetRepository petRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PetService petService;

    @Autowired
    PetTagRepository petTagRepository;

    @Test
    @DisplayName("account에 자신의 Pet을 저장할 수 있다 - 이미지 없는 경우")
    void registerPetWithoutImage() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();
        PetRequest petRequest = new PetRequest();
        petRequest.setName("name");
        petRequest.setYear(2021);
        petRequest.setMonth(8);
        petRequest.setBreed("말티즈");
        petRequest.setSex(PetSex.MALE);
        petRequest.setNeutering(true);
        petRequest.setSizeType(PetSizeType.MEDIUM);
        petRequest.setTags(List.of("활발", "사나움"));
        petRequest.setImageFile(null);

        //when
        long savedId = petService.addPet(account, petRequest);

        Pet savedPet = petRepository.findById(savedId).get();

        //then
        assertThat(savedPet.getName()).isEqualTo("name");
        assertThat(savedPet.getAge().getAge()).isEqualTo("10개월");
        assertThat(savedPet.getAccount()).isEqualTo(account);
        assertThat(savedPet.getTags().size()).isEqualTo(2);
    }

    List<MultipartFile> createMockImageFiles() {
        List<MultipartFile> mockFiles = new ArrayList<>();

        try {
            MockMultipartFile mockMultipartFile1 = new MockMultipartFile(
                    "mockImage1",
                    "cat.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    new FileInputStream(new File("src/test/resources/assets/cat.jpg")));

            MockMultipartFile mockMultipartFile2 = new MockMultipartFile(
                    "mockImage2",
                    "city.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    new FileInputStream(new File("src/test/resources/assets/city.jpg")));

            mockFiles.add(mockMultipartFile1);
            mockFiles.add(mockMultipartFile2);
        } catch (Exception e) {
        }

        return mockFiles;
    }

    @Test
    @DisplayName("account에 자신의 Pet을 저장할 수 있다 - 이미지 있는 경우")
    void registerPetWithImage() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();
        PetRequest petRequest = new PetRequest();
        petRequest.setName("name");
        petRequest.setYear(2021);
        petRequest.setMonth(8);
        petRequest.setBreed("말티즈");
        petRequest.setSex(PetSex.MALE);
        petRequest.setNeutering(true);
        petRequest.setSizeType(PetSizeType.MEDIUM);
        petRequest.setTags(List.of("활발", "사나움"));
        petRequest.setImageFile(createMockImageFiles().get(0));

        //when
        long savedId = petService.addPet(account, petRequest);

        Pet savedPet = petRepository.findById(savedId).get();

        //then
        assertThat(savedPet.getName()).isEqualTo("name");
        assertThat(savedPet.getAge().getAge()).isEqualTo("10개월");
        assertThat(savedPet.getAccount()).isEqualTo(account);
        assertThat(savedPet.getTags().size()).isEqualTo(2);
        assertThat(savedPet.getPetImage()).isNotNull();
    }

    @Test
    @DisplayName("Pet 정보 삭제 시 PetTag도 같이 삭제 된다")
    void deletePetInfoWithPetTag() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();
        PetRequest petRequest = new PetRequest();
        petRequest.setName("name");
        petRequest.setYear(2021);
        petRequest.setMonth(8);
        petRequest.setBreed("말티즈");
        petRequest.setSex(PetSex.MALE);
        petRequest.setNeutering(true);
        petRequest.setSizeType(PetSizeType.MEDIUM);
        petRequest.setTags(List.of("활발", "사나움"));
        petRequest.setImageFile(createMockImageFiles().get(0));

        long savedId = petService.addPet(account, petRequest);

        //when
        petService.deletePetInfo(savedId);

        //then
        assertThat(petTagRepository.findAll().size()).isEqualTo(5);
    }
}