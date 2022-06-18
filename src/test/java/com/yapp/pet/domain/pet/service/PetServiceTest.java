package com.yapp.pet.domain.pet.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet.entity.PetSex;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.pet_tag.PetTag;
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

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("account에 자신의 Pet을 저장할 수 있다")
    void registerPetToAccount() throws Exception {
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
        petRequest.setTags(null);
        petRequest.setImageFile(null);

        //when
        long savedId = petService.addPet(account, petRequest);

        //then

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
}