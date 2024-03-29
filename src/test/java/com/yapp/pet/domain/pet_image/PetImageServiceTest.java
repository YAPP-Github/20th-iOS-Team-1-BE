package com.yapp.pet.domain.pet_image;

import com.yapp.pet.global.util.s3.S3Utils;
import com.yapp.pet.support.AbstractIntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PetImageService Integration Test")
@RequiredArgsConstructor
class PetImageServiceTest extends AbstractIntegrationTest {

    private final PetImageService petImageService;
    private final S3Utils s3Utils;

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
}