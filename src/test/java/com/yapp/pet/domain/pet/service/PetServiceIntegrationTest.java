package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.entity.PetSex;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_image.PetImageService;
import com.yapp.pet.domain.pet_tag.PetTagRepository;
import com.yapp.pet.support.AbstractIntegrationTest;
import com.yapp.pet.web.pet.model.PetRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PerService Integration Test")
class PetServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PetRepository petRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PetService petService;

    @Autowired
    PetTagRepository petTagRepository;

    @Autowired
    PetImageService petImageService;

    @Test
    @DisplayName("addPet() : account와 Pet 정보가 정상적으로 요청될 경우 Pet Image가 없어도 account는 Pet을 저장할 수 있다")
    void testAddPetWithoutPetImage() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();

        PetRequest petRequest = PetRequest.builder()
                                          .name("name")
                                          .year(2021)
                                          .month(8)
                                          .breed("말티즈")
                                          .sex(PetSex.MALE)
                                          .neutering(true)
                                          .sizeType(PetSizeType.MEDIUM)
                                          .tags(List.of("활발", "사나움"))
                                          .imageFile(null)
                                          .build();

        //when
        long savedId = petService.addPet(account, petRequest);

        Pet savedPet = petRepository.findById(savedId).get();

        //then
        assertThat(savedPet.getName()).isEqualTo("name");
        assertThat(savedPet.getAccount()).isEqualTo(account);
        assertThat(savedPet.getTags().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("addPet() : account와 Pet 정보가 정상적으로 요청될 경우 Pet Image에 상관없이 account는 Pet을 저장할 수 있다")
    void testAddPetWithImage() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();

        List<MultipartFile> mockImageFiles = createMockImageFiles();

        MultipartFile multipartFile = mockImageFiles.get(0);

        PetRequest petRequest = PetRequest.builder()
                                          .name("name")
                                          .year(2021)
                                          .month(8)
                                          .breed("말티즈")
                                          .sex(PetSex.MALE)
                                          .neutering(true)
                                          .sizeType(PetSizeType.MEDIUM)
                                          .tags(List.of("활발", "사나움"))
                                          .imageFile(multipartFile)
                                          .build();

        //when
        long savedId = petService.addPet(account, petRequest);

        Pet savedPet = petRepository.findById(savedId).get();

        //then
        assertThat(savedPet.getName()).isEqualTo("name");
        assertThat(savedPet.getAccount()).isEqualTo(account);
        assertThat(savedPet.getTags().size()).isEqualTo(2);
        assertThat(savedPet.getPetImage()).isNotNull();
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
    @DisplayName("Pet 정보 삭제 시 PetTag도 같이 삭제 된다")
    void deletePetInfoWithPetTag() throws Exception {
        //given
        Account account = accountRepository.findById(1L).get();

        PetRequest petRequest = PetRequest.builder()
                                          .name("name")
                                          .year(2021)
                                          .month(8)
                                          .breed("말티즈")
                                          .sex(PetSex.MALE)
                                          .neutering(true)
                                          .sizeType(PetSizeType.MEDIUM)
                                          .tags(List.of("활발", "사나움"))
                                          .imageFile(null)
                                          .build();

        long savedId = petService.addPet(account, petRequest);

        //when
        petService.deletePetInfo(savedId);

        //then
        assertThat(petTagRepository.findAll().size()).isEqualTo(5);
    }
}