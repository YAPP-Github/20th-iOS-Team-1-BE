package com.yapp.pet.domain.pet_image;

import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.global.util.s3.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.yapp.pet.global.TogaetherConstants.S3_ACCOUNT_DIR_NAME;
import static com.yapp.pet.global.TogaetherConstants.S3_PET_DIR_NAME;

@Service
@RequiredArgsConstructor
@Transactional
public class PetImageService {

    private final S3Utils s3Utils;

    private final PetImageRepository petImageRepository;

    public PetImage create(MultipartFile imageFile) {

        String origFilename = imageFile.getOriginalFilename();
        String filename = s3Utils.createFilename(origFilename);
        String imageUrl = s3Utils.putS3(imageFile, filename, S3_PET_DIR_NAME);
        String s3Key = s3Utils.createS3Key(filename, S3_PET_DIR_NAME);

        PetImage petImage = PetImage.builder()
                                    .name(filename)
                                    .originName(origFilename)
                                    .s3Key(s3Key)
                                    .path(imageUrl)
                                    .build();

        petImageRepository.save(petImage);

        return petImage;
    }

    public void delete(Pet pet){
        petImageRepository.delete(pet.getPetImage());

        s3Utils.deleteToS3(pet.getPetImage());
    }
}
