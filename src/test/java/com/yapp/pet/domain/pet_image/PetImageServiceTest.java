package com.yapp.pet.domain.pet_image;

import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.account_image.AccountImageService;
import com.yapp.pet.global.util.s3.S3Utils;
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
class PetImageServiceTest {

    @Autowired
    PetImageService petImageService;

    @Autowired
    AccountImageService accountImageService;

    @Autowired
    S3Utils s3Utils;

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
    @DisplayName("이미지를 S3에 저장하고 제대로 저장되었는지 확인한다")
    void isRegisterPetImage() throws Exception {
        //given
        List<MultipartFile> mockImageFiles = createMockImageFiles();

        //when
        MultipartFile multipartFile = mockImageFiles.get(0);

        PetImage petImage = petImageService.create(multipartFile);

        //then
        assertThat(s3Utils.getImagePath(petImage.getPath())).isNotNull();
    }

    @Test
    @DisplayName("S3에서 이미지를 삭제할 수 있다")
    void deletePetImage() throws Exception {
        //given
        List<MultipartFile> mockImageFiles = createMockImageFiles();

        //when
        MultipartFile multipartFile = mockImageFiles.get(0);

        PetImage petImage = petImageService.create(multipartFile);

        s3Utils.deleteToS3(petImage);

        //then
        assertThat(s3Utils.getImagePath(petImage.getPath())).isNull();
    }
}